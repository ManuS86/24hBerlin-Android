package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepository
import com.example.a24hberlin.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val eventRepo = EventRepository(EventApi)
    private val TAG = "EventViewModel"
    private val userRepo = UserRepository(db)

    private var _currentUser = MutableLiveData<AppUser?>()
    val currentUser: LiveData<AppUser?>
        get() = _currentUser

    private var _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>>
        get() = _events

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            try {
                val allEvents = eventRepo.loadEvents()

                allEvents.let { eventsMap ->
                    eventsMap.map { (id, event) -> // setEventIDs
                        event.id = id
                        event
                    }
                }.let { eventsWithIDs ->
                    eventsWithIDs.flatMap { event -> // expandEventsWithRepeats
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
                    expandedEvents.filter { event -> // filterAndSort
                        event.start.toLocalDate() >= LocalDate.now()
                    }.sortedBy { it.start }
                }.let { finalEvents ->
                    _events.value = finalEvents
                }
            } catch (ex: Exception) {
                Log.e("EventsApiCall", ex.toString())
            }
        }
    }
}