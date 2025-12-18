package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.enums.EventReminderType
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepositoryImpl
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.managers.AndroidPermissionManager
import com.example.a24hberlin.notifications.schedule.AndroidReminderScheduler
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "EventViewModel"

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val permissionManager = AndroidPermissionManager(application)
    private val reminderScheduler = AndroidReminderScheduler(application.applicationContext)

    private val db = FirebaseFirestore.getInstance()
    private val eventRepo = EventRepositoryImpl(EventApi)
    private val userRepo = UserRepositoryImpl(db)

    private var userListener: ListenerRegistration? = null

    private val _currentAppUser = MutableStateFlow<AppUser?>(null)
    val currentAppUser = _currentAppUser.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events
        .onStart { loadEvents() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val hasNotificationPermission: StateFlow<Boolean> = permissionManager.hasNotificationPermission

    val bookmarks: StateFlow<List<Event>> = combine(currentAppUser, events) { user, eventsList ->
        user?.let {
            eventsList.filter { event ->
                it.bookmarkIDs.contains(event.id)
            }
        } ?: emptyList()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val uniqueLocations: StateFlow<List<String>> = events.map { eventsList ->
        eventsList.mapNotNull { it.locationName }
            .map { name ->
                name.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
            .distinct()
            .sorted()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val uniqueSounds: StateFlow<List<String>> = events.map { eventsList ->
        eventsList.flatMap { event ->
            event.sounds?.values?.toList() ?: emptyList()
        }
            .distinct()
            .sorted()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    init {
        if (userListener == null) {
            userListener = userRepo.addUserListener { user ->
                _currentAppUser.value = user
            }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val finalEvents = eventRepo.getEventsWithProcessedData()
                _events.emit(finalEvents)
            } catch (ex: Exception) {
                Log.e(TAG, "Error loading events from repository.", ex)
            }
        }
    }

    fun addBookmarkId(bookmarkId: String) {
        val event = _events.value.find { it.id == bookmarkId } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.updateUserInformation(bookmarkId, null)

                currentAppUser.value?.settings?.let { settings ->
                    if (settings.pushNotificationsEnabled) {
                        addBookmarkPushNotifications(event)
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error adding bookmark ID: $bookmarkId.", ex)
            }
        }
    }

    fun removeBookmarkId(bookmarkId: String) {
        val event = _events.value.find { it.id == bookmarkId } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.removeBookmarkId(bookmarkId)

                currentAppUser.value?.settings?.let { settings ->
                    if (settings.pushNotificationsEnabled) {
                        reminderScheduler.cancelEventReminders(event)
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error removing bookmark ID: $bookmarkId.", ex)
            }
        }
    }

    fun addBookmarkPushNotifications(event: Event) {
        reminderScheduler.scheduleEventReminder(
            event,
            EventReminderType.THREE_DAYS_BEFORE,
            event.imageURL
        )
        reminderScheduler.scheduleEventReminder(
            event,
            EventReminderType.TWELVE_HOURS_BEFORE,
            event.imageURL
        )
        reminderScheduler.scheduleEventReminder(
            event,
            EventReminderType.THREE_HOURS_BEFORE,
            event.imageURL
        )
    }

    fun setupAbsenceReminder() {
        reminderScheduler.schedule14DayReminder()
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
        userListener = null
    }
}