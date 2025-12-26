package com.esutor.twentyfourhoursberlin.ui.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esutor.twentyfourhoursberlin.data.enums.EventReminderType
import com.esutor.twentyfourhoursberlin.data.enums.EventType
import com.esutor.twentyfourhoursberlin.data.enums.Month
import com.esutor.twentyfourhoursberlin.data.model.AppUser
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.data.repository.events.EventRepository
import com.esutor.twentyfourhoursberlin.data.repository.user.UserRepository
import com.esutor.twentyfourhoursberlin.managers.permissionmanager.AndroidPermissionManager
import com.esutor.twentyfourhoursberlin.notifications.reminderscheduler.AndroidReminderScheduler
import com.esutor.twentyfourhoursberlin.utils.filteredEvents
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
import kotlinx.coroutines.withContext

data class EventFilters(
    val month: Month? = null,
    val type: EventType? = null,
    val sound: String? = null,
    val venue: String? = null
)

class EventViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val eventRepo: EventRepository,
    private val userRepo: UserRepository,
    permissionManager: AndroidPermissionManager,
    private val reminderScheduler: AndroidReminderScheduler
) : ViewModel() {

    companion object {
        private const val TAG = "EventViewModel"
        private const val KEY_SEARCH_TEXT = "search_text"
        private const val KEY_EVENT_TYPE = "selected_event_type"
        private const val KEY_MONTH = "selected_month"
        private const val KEY_SOUND = "selected_sound"
        private const val KEY_VENUE = "selected_venue"
    }

    // --- UI & App States ---
    private var userListener: ListenerRegistration? = null

    private val _currentAppUser = MutableStateFlow<AppUser?>(null)
    val currentAppUser = _currentAppUser.asStateFlow()

    private val _events = MutableStateFlow<List<Event>?>(null)
    private val events: StateFlow<List<Event>?> = _events
        .onStart { loadEvents() }
        .stateIn(viewModelScope, WhileSubscribed(5000L), null)

    private val _isLoading: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoading = _isLoading.asStateFlow()

    private val _searchTextFieldValue = MutableStateFlow(
        TextFieldValue(savedStateHandle.get<String>(KEY_SEARCH_TEXT) ?: "")
    )
    val searchTextFieldValue = _searchTextFieldValue.asStateFlow()

    val hasNotificationPermission = permissionManager.hasNotificationPermission

    // --- Filter Inputs (SavedStateHandle) ---
    val selectedEventType = savedStateHandle.getStateFlow<EventType?>(KEY_EVENT_TYPE, null)
    val selectedMonth = savedStateHandle.getStateFlow<Month?>(KEY_MONTH, null)
    val selectedSound = savedStateHandle.getStateFlow<String?>(KEY_SOUND, null)
    val selectedVenue = savedStateHandle.getStateFlow<String?>(KEY_VENUE, null)

    private val filterCriteria = combine(
        selectedMonth, selectedEventType, selectedSound, selectedVenue
    ) { month, type, sound, venue ->
        EventFilters(month, type, sound, venue)
    }.stateIn(viewModelScope, WhileSubscribed(5000L), EventFilters())

    // --- Computed Outputs (Derived Lists) ---
    val filteredEvents: StateFlow<List<Event>?> = combine(
        events, searchTextFieldValue, filterCriteria
    ) { eventsList, textValue, filters ->
        if (eventsList == null) return@combine null

        withContext(Dispatchers.Default) {
            filteredEvents(
                events = eventsList,
                searchText = textValue,
                selectedMonth = filters.month,
                selectedEventType = filters.type,
                selectedSound = filters.sound,
                selectedVenue = filters.venue
            )
        }
    }.stateIn(viewModelScope, WhileSubscribed(5000L), null)

    val bookmarks: StateFlow<List<Event>?> = combine(currentAppUser, events) { user, eventsList ->
        if (user == null || eventsList == null) return@combine null

        val bookmarkIds = user.bookmarkIDs.toSet()
        eventsList.filter { it.id in bookmarkIds }
    }.stateIn(viewModelScope, WhileSubscribed(5000L), null)

    val filteredBookmarks = combine(
        bookmarks,
        searchTextFieldValue
    ) { bookmarkedList, textValue ->
        if (bookmarkedList == null) return@combine null

        withContext(Dispatchers.Default) {
            filteredEvents(
                events = bookmarkedList,
                searchText = textValue,
                selectedMonth = null,
                selectedEventType = null,
                selectedSound = null,
                selectedVenue = null
            )
        }
    }.stateIn(viewModelScope, WhileSubscribed(5000L), null)

    val uniqueLocations = events.map { list ->
        withContext(Dispatchers.Default) {
            list?.mapNotNull { it.locationName?.trim()?.replaceFirstChar { c -> c.uppercase() } }
                ?.distinct()?.sorted() ?: emptyList()
        }
    }.stateIn(viewModelScope, WhileSubscribed(5000L), emptyList())

    val uniqueSounds: StateFlow<List<String>> = events.map { list ->
        withContext(Dispatchers.Default) {
            list?.flatMap { it.sounds?.values ?: emptyList() }
                ?.distinct()?.sorted() ?: emptyList()
        }
    }.stateIn(viewModelScope, WhileSubscribed(5000L), emptyList())

    // --- Lifecycle & Data Loading ---
    init {
        userListener = userRepo.addUserListener { user -> _currentAppUser.value = user }
    }

    private fun loadEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val finalEvents = eventRepo.getProcessedEvents()
                _events.value = finalEvents
            } catch (e: Exception) {
                Log.e(TAG, "Error loading events from repository.", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Filter & Search actions ---
    fun updateSearchText(newValue: TextFieldValue) {
        _searchTextFieldValue.value = newValue
        savedStateHandle[KEY_SEARCH_TEXT] = newValue.text
    }

    private fun <T> toggleFilter(key: String, currentValue: T?, newValue: T?) {
        savedStateHandle[key] = if (currentValue == newValue) null else newValue
    }

    fun updateEventType(type: EventType?) =
        toggleFilter(KEY_EVENT_TYPE, selectedEventType.value, type)

    fun updateMonth(month: Month?) = toggleFilter(KEY_MONTH, selectedMonth.value, month)
    fun updateSound(sound: String?) = toggleFilter(KEY_SOUND, selectedSound.value, sound)
    fun updateVenue(venue: String?) = toggleFilter(KEY_VENUE, selectedVenue.value, venue)

    fun clearAllFilters() {
        savedStateHandle[KEY_EVENT_TYPE] = null
        savedStateHandle[KEY_MONTH] = null
        savedStateHandle[KEY_SOUND] = null
        savedStateHandle[KEY_VENUE] = null
    }

    // --- Event & Bookmark actions ---
    fun addBookmarkId(bookmarkId: String) {
        val event = events.value?.find { it.id == bookmarkId } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.updateUserInformation(bookmarkId, null)

                if (currentAppUser.value?.settings?.notificationsEnabled == true) {
                    addBookmarkReminder(event)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error adding bookmark ID: $bookmarkId.", ex)
            }
        }
    }

    fun removeBookmarkId(bookmarkId: String) {
        val event = events.value?.find { it.id == bookmarkId } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.removeBookmarkId(bookmarkId)

                if (currentAppUser.value?.settings?.notificationsEnabled == true) {
                    reminderScheduler.cancelEventReminders(event)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error removing bookmark ID: $bookmarkId.", ex)
            }
        }
    }

    fun addBookmarkReminder(event: Event) {
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

    fun setupAbsenceReminder() = reminderScheduler.schedule14DayReminder()

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
    }
}