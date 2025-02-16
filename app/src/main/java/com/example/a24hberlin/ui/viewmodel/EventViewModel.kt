package com.example.a24hberlin.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepository
import com.example.a24hberlin.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class EventViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val eventRepo = EventRepository(EventApi)
    private var listener: ListenerRegistration? = null
    private val TAG = "EventViewModel"
    private val userRepo = UserRepository(db)

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

                eventResponse.let { eventsMap ->
                    eventsMap.map { (id, event) -> // Set event IDs
                        event.id = id
                        event
                    }
                }.let { eventsWithIDs ->
                    eventsWithIDs.flatMap { event -> // Create repeat copies
                        listOf(event) + (event.repeats?.mapIndexed { index, repeatData ->
                            event.copy(
                                id = "${event.id}-${index}",
                                start = repeatData[0].let {
                                    Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()
                                },
                                end = repeatData.getOrNull(1)?.let {
                                    Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()
                                },
                                repeats = null
                            )
                        } ?: emptyList())
                    }
                }.let { expandedEvents ->
                    expandedEvents.filter { event ->
                        val now = LocalDate.now()
                        val eventDate = event.start.toLocalDate()

                        eventDate.isEqual(now) || eventDate.isAfter(now) // Today or later
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
        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(favoriteID)
                // Optionally update the events list to reflect the change locally
                // This would require fetching the updated user data and events
            } catch (ex: Exception) {
                Log.e("Add Favorite ID", ex.toString())
            }
        }
    }

    fun removeFavoriteID(favoriteID: String) {
        viewModelScope.launch {
            try {
                userRepo.removeFavoriteID(favoriteID)
                // Optionally update the events list to reflect the change locally
                // This would require fetching the updated user data and events
            } catch (ex: Exception) {
                Log.e("Remove Favorite ID", ex.toString())
            }
        }
    }

    fun addFavoritePushNotification(event: Event, dayModifier: Int, hourModifier: Int) {

    }

    fun setupAbsenceReminder() {

    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}