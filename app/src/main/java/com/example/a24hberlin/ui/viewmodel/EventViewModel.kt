package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepository
import com.example.a24hberlin.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val eventRepo = EventRepository(EventApi)
    private var listener: ListenerRegistration? = null
    private val TAG = "EventViewModel"
    private val userRepo = UserRepository(db)

    var currentAppUser by mutableStateOf<AppUser?>(null)
        private set

    var events by mutableStateOf<List<Event>>(emptyList())
        private set

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
                val allEvents = eventRepo.loadEvents()

                allEvents.let { eventsMap ->
                    eventsMap.map { (id, event) ->
                        event.id = id
                        event
                    }
                }.let { eventsWithIDs ->
                    eventsWithIDs.flatMap { event ->
                        listOf(event) + (event.repeats?.mapIndexed { index, repeatData ->
                            event.copy(
                                id = "${event.id}-${index}",
                                start = repeatData.first,
                                end = repeatData.second,
                                repeats = null
                            )
                        } ?: emptyList())
                    }
                }.let { expandedEvents ->
                    expandedEvents.filter { event ->
                        event.start.toLocalDate() >= LocalDate.now()
                    }.sortedBy { it.start }
                }.let { finalEvents ->
                    events = finalEvents // Update the MutableState
                }
            } catch (ex: Exception) {
                Log.e("EventsApiCall", ex.toString())
            }
        }
    }

    fun addFavoriteID(favoriteID: String) {
        viewModelScope.launch {
            try {
                userRepo.addFavoriteID(favoriteID)
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