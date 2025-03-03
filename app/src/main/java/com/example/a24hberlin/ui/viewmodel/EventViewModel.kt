package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val currentAppUser: StateFlow<AppUser?> = _currentAppUser.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    val hasNotificationPermission: StateFlow<Boolean> = permissionRepo.hasNotificationPermission

    val favorites by derivedStateOf {
        _currentAppUser.value?.let { user ->
            _events.value.filter { event ->
                user.favoriteIDs.contains(event.id)
            }
        }
    }

    val uniqueLocations by derivedStateOf {
        _events.value.mapNotNull { it.locationName }
            .distinct()
            .sorted()
    }

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                _currentAppUser.value = user
            }
        }
        loadEvents()
    }

    fun loadEvents() {
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
                    _events.value = finalEvents
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
                if (_currentAppUser.value?.settings!!.pushNotificationsEnabled) {
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
                if (_currentAppUser.value?.settings!!.pushNotificationsEnabled) {
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
                        11,
                        image
                    )
                    notificationService.scheduleEventReminder(
                        event,
                        0,
                        11,
                        image
                    )
                    notificationService.scheduleEventReminder(
                        event,
                        0,
                        2,
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