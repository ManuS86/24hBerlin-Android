package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.enums.EventReminderType
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.repository.EventRepositoryImpl
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.managers.AndroidPermissionManager
import com.example.a24hberlin.notifications.schedule.AndroidReminderScheduler
import com.example.a24hberlin.utils.filteredEvents
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EventFilters(
    val month: Month? = null,
    val type: EventType? = null,
    val sound: String? = null,
    val venue: String? = null
)

class EventViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "EventViewModel"
        private const val KEY_SEARCH_TEXT = "search_text"
        private const val KEY_EVENT_TYPE = "selected_event_type"
        private const val KEY_MONTH = "selected_month"
        private const val KEY_SOUND = "selected_sound"
        private const val KEY_VENUE = "selected_venue"
    }

    // --- REPOSITORIES & MANAGERS ---
    private val permissionManager = AndroidPermissionManager(application)
    private val reminderScheduler = AndroidReminderScheduler(application.applicationContext)
    private val eventRepo = EventRepositoryImpl(EventApi)
    private val userRepo = UserRepositoryImpl(FirebaseFirestore.getInstance())

    // --- DATA STATES ---
    private var userListener: ListenerRegistration? = null

    private val _currentAppUser = MutableStateFlow<AppUser?>(null)
    val currentAppUser = _currentAppUser.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    private val events: StateFlow<List<Event>> = _events
        .onStart { loadEvents() }
        .stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    // --- FILTER & SEARCH STATES ---
    val selectedEventType = savedStateHandle.getStateFlow<EventType?>(KEY_EVENT_TYPE, null)
    val selectedMonth = savedStateHandle.getStateFlow<Month?>(KEY_MONTH, null)
    val selectedSound = savedStateHandle.getStateFlow<String?>(KEY_SOUND, null)
    val selectedVenue = savedStateHandle.getStateFlow<String?>(KEY_VENUE, null)

    private val _searchTextFieldValue = MutableStateFlow(
        TextFieldValue(savedStateHandle.get<String>(KEY_SEARCH_TEXT) ?: "")
    )
    val searchTextFieldValue: StateFlow<TextFieldValue> = _searchTextFieldValue.asStateFlow()

    val hasNotificationPermission: StateFlow<Boolean> = permissionManager.hasNotificationPermission

    // --- COMPUTED FLOWS ---
    private val filterCriteria = combine(
        selectedMonth,
        selectedEventType,
        selectedSound,
        selectedVenue
    ) { month, type, sound, venue ->
        EventFilters(month, type, sound, venue)
    }.stateIn(viewModelScope, WhileSubscribed(5000), EventFilters())

    val filteredEvents: StateFlow<List<Event>> = combine(
        events,
        searchTextFieldValue,
        filterCriteria
    ) { eventsList, textValue, filters ->
        filteredEvents(
            events = eventsList,
            searchText = textValue,
            selectedMonth = filters.month,
            selectedEventType = filters.type,
            selectedSound = filters.sound,
            selectedVenue = filters.venue
        )
    }.stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<Event>> = combine(currentAppUser, events) { user, eventsList ->
        val bookmarkIds = user?.bookmarkIDs?.toSet() ?: emptySet()
        if (bookmarkIds.isEmpty()) emptyList()
        else eventsList.filter { it.id in bookmarkIds }
    }.stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    val filteredBookmarks = combine(
        bookmarks,
        searchTextFieldValue
    ) { bookmarkedList, textValue ->
        filteredEvents(
            events = bookmarkedList,
            searchText = textValue,
            selectedMonth = null,
            selectedEventType = null,
            selectedSound = null,
            selectedVenue = null
        )
    }.stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    // --- DATA HELPERS ---
    val uniqueLocations: StateFlow<List<String>> = events.map { eventsList ->
        eventsList.mapNotNull { it.locationName?.trim()?.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }}
            .distinct()
            .sorted()
    }.stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    val uniqueSounds = events.map { list ->
        list.flatMap { it.sounds?.values ?: emptyList() }.distinct().sorted()
    }.stateIn(viewModelScope, WhileSubscribed(5000), emptyList())

    // --- INIT & LIFECYCLE ---
    init {
        userListener = userRepo.addUserListener { user -> _currentAppUser.value = user }
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
    }

    // --- ACTIONS: FILTERS & SEARCH ---
    fun updateEventType(type: EventType?) {
        savedStateHandle[KEY_EVENT_TYPE] = if (selectedEventType.value == type) null else type
    }

    fun updateSearchText(newValue: TextFieldValue) {
        _searchTextFieldValue.value = newValue
        savedStateHandle[KEY_SEARCH_TEXT] = newValue.text
    }

    fun updateMonth(month: Month?) {
        savedStateHandle[KEY_MONTH] = if (selectedMonth.value == month) null else month
    }

    fun updateSound(sound: String?) {
        savedStateHandle[KEY_SOUND] = if (selectedSound.value == sound) null else sound
    }

    fun updateVenue(venue: String?) {
        savedStateHandle[KEY_VENUE] = if (selectedVenue.value == venue) null else venue
    }

    fun clearAllFilters() {
        savedStateHandle[KEY_EVENT_TYPE] = null
        savedStateHandle[KEY_MONTH] = null
        savedStateHandle[KEY_SOUND] = null
        savedStateHandle[KEY_VENUE] = null
    }

    // --- ACTIONS: EVENTS & BOOKMARKS ---
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

                if (currentAppUser.value?.settings?.pushNotificationsEnabled == true) {
                    addBookmarkPushNotifications(event)
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

                if (currentAppUser.value?.settings?.pushNotificationsEnabled == true) {
                    reminderScheduler.cancelEventReminders(event)
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
}