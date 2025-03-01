package com.example.a24hberlin.ui.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepository
import com.example.a24hberlin.data.repository.UserRepository
import com.example.a24hberlin.utils.services.NotificationService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val eventRepo = EventRepository(EventApi)
    private var listener: ListenerRegistration? = null
    private val userRepo = UserRepository(db)
    private val notificationService = NotificationService(application.applicationContext)

    var currentAppUser by mutableStateOf<AppUser?>(null)
        private set

    var events by mutableStateOf<List<Event>>(emptyList())
        private set

    val favorites by derivedStateOf {
        currentAppUser?.let { user ->
            events.filter { event ->
                user.favoriteIDs.contains(event.id)
            }
        }
    }

    var hasNotificationPermission by
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    } else mutableStateOf(true)

    val uniqueLocations by derivedStateOf {
        events.mapNotNull { it.locationName }
            .distinct()
            .sorted()
    }

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                currentAppUser = user
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
                    events = finalEvents
                }
            } catch (ex: Exception) {
                Log.e("EventsApiCall", ex.toString())
            }
        }
    }

    fun addFavoriteID(favoriteID: String) {
        val event = events.find { it.id == favoriteID } ?: return

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(favoriteID)
                if (currentAppUser?.settings!!.pushNotificationsEnabled) {
                    notificationService.scheduleEventReminder(event, 3, 11)
                    notificationService.scheduleEventReminder(event, 0, 11)
                    notificationService.scheduleEventReminder(event, 0, 2)
                }
            } catch (ex: Exception) {
                Log.e("Add Favorite ID", ex.toString())
            }
        }
    }

    fun removeFavoriteID(favoriteID: String) {
        val event = events.find { it.id == favoriteID } ?: return

        viewModelScope.launch {
            try {
                userRepo.removeFavoriteID(favoriteID)
                if (!currentAppUser?.settings!!.pushNotificationsEnabled) {
                    notificationService.unscheduleEventReminder(event)
                }
            } catch (ex: Exception) {
                Log.e("Remove Favorite ID", ex.toString())
            }
        }
    }

    fun addFavoritePushNotification(event: Event, dayModifier: Int, hourModifier: Int) {
        notificationService.scheduleEventReminder(event, dayModifier, hourModifier)

    }

    fun setupAbsenceReminder() {
        notificationService.updateLastAppOpenDate()
        notificationService.schedule14DayReminder()
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}