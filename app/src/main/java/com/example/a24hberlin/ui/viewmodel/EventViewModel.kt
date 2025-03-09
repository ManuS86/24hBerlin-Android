package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepositoryImpl
import com.example.a24hberlin.data.repository.PermissionRepositoryImpl
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.services.NotificationService
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
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.LocalDate

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val eventRepo = EventRepositoryImpl(EventApi)
    private val permissionRepo = PermissionRepositoryImpl(application)
    private var listener: ListenerRegistration? = null
    private val userRepo = UserRepositoryImpl(db)
    private val notificationService = NotificationService(application.applicationContext)

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

    val hasNotificationPermission: StateFlow<Boolean> = permissionRepo.hasNotificationPermission

    val favorites: StateFlow<List<Event>> = combine(currentAppUser, events) { user, eventsList ->
        user?.let {
            eventsList.filter { event ->
                it.favoriteIDs.contains(event.id)
            }
        } ?: emptyList()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val uniqueLocations: StateFlow<List<String>> = events.map { eventsList ->
        eventsList.mapNotNull { it.locationName }
            .distinct()
            .sorted()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                _currentAppUser.value = user
            }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            try {
                val eventResponse = eventRepo.loadEvents()

                eventResponse.let { eventsMap -> // Set event IDs
                    eventsMap.map { (id, event) ->
                        event.id = id
                        event
                    }
                }.let { eventsWithIDs -> // Create repeat copies
                    eventsWithIDs.flatMap { event ->
                        buildList {
                            add(event)

                            event.repeats?.forEachIndexed { index, repeatData ->
                                if (index == 0) return@forEachIndexed

                                val startTimeSeconds = repeatData.getOrNull(0)
                                val endTimeSeconds = repeatData.getOrNull(1)

                                add(
                                    event.copy(
                                        id = "${event.id}-${index}",
                                        startSecs = startTimeSeconds?.toString() ?: event.startSecs,
                                        endSecs = endTimeSeconds,
                                        repeats = null
                                    )
                                )
                            }
                        }
                    }
                }.let { expandedEvents -> // Today or later
                    expandedEvents.filter { event ->
                        val now = LocalDate.now()
                        val eventDate = event.start.toLocalDate()

                        eventDate.isEqual(now) || eventDate.isAfter(now)
                    }.sortedBy { it.start }
                }.let { finalEvents ->
                    _events.emit(finalEvents)
                }
            } catch (ex: Exception) {
                Log.e("EventsApiCall", ex.toString())
            }
        }
    }

    fun addFavoriteID(favoriteID: String) {
        val event = _events.value.find { it.id == favoriteID } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.updateUserInformation(favoriteID, null)
                if (currentAppUser.value?.settings!!.pushNotificationsEnabled) {
                    addFavoritePushNotifications(event)
                }
            } catch (ex: Exception) {
                Log.e("Add Favorite ID", ex.toString())
            }
        }
    }

    fun removeFavoriteID(favoriteID: String) {
        val event = _events.value.find { it.id == favoriteID } ?: return

        viewModelScope.launch {
            try {
                userRepo.removeFavoriteID(favoriteID)
                if (currentAppUser.value?.settings!!.pushNotificationsEnabled) {
                    notificationService.unscheduleEventReminder(event)
                }
            } catch (ex: Exception) {
                Log.e("Remove Favorite ID", ex.toString())
            }
        }
    }

    fun addFavoritePushNotifications(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(event.imageURL)
                val input = url.openStream()
                val image = BitmapFactory.decodeStream(input)

                withContext(Dispatchers.Main) {
                    notificationService.scheduleEventReminder(
                        event,
                        3,
                        12,
                        image
                    )
                    notificationService.scheduleEventReminder(
                        event,
                        0,
                        12,
                        image
                    )
                    notificationService.scheduleEventReminder(
                        event,
                        0,
                        3,
                        image
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setupAbsenceReminder() {
        notificationService.schedule14DayReminder()
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}