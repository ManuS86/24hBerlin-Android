package com.esutor.twentyfourhoursberlin.ui.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esutor.twentyfourhoursberlin.data.enums.EventReminderType
import com.esutor.twentyfourhoursberlin.data.enums.EventType
import com.esutor.twentyfourhoursberlin.data.enums.Month
import com.esutor.twentyfourhoursberlin.data.models.AppUser
import com.esutor.twentyfourhoursberlin.data.models.Event
import com.esutor.twentyfourhoursberlin.data.repositories.events.EventRepository
import com.esutor.twentyfourhoursberlin.data.repositories.user.UserRepository
import com.esutor.twentyfourhoursberlin.managers.internetconnectionobserver.ConnectivityObserver
import com.esutor.twentyfourhoursberlin.managers.permissionmanager.AndroidPermissionManager
import com.esutor.twentyfourhoursberlin.notifications.reminderscheduler.AndroidReminderScheduler
import com.esutor.twentyfourhoursberlin.utils.filteredEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val reminderScheduler: AndroidReminderScheduler,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    // region Constants
    companion object {
        private const val TAG = "EventViewModel"
        private const val KEY_SEARCH_TEXT = "search_text"
        private const val KEY_EVENT_TYPE = "selected_event_type"
        private const val KEY_MONTH = "selected_month"
        private const val KEY_SOUND = "selected_sound"
        private const val KEY_VENUE = "selected_venue"
    }
    // endregion

    // region Base Data & Connection
    private val _events = MutableStateFlow<List<Event>?>(null)
    val events: StateFlow<List<Event>?> = _events
        .onStart {
            if (_events.value == null) {
                _isLoading.value = true
                observeConnectivity()

                if (!connectivityObserver.isConnected.first()) {
                    _isLoading.value = false
                }
            }
        }
        .stateIn(viewModelScope, WhileSubscribed(5000L), null)

    // Needs to start null to prevent LoadingScreen animation to play too early
    private val _isLoading: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoading = _isLoading.asStateFlow()

    val currentAppUser: StateFlow<AppUser?> = userRepo.getUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = null
        )
    // endregion

    // region Search & Filter Inputs
    private val _searchTextFieldValue = MutableStateFlow(
        TextFieldValue(savedStateHandle.get<String>(KEY_SEARCH_TEXT) ?: "")
    )
    val searchTextFieldValue = _searchTextFieldValue.asStateFlow()

    val selectedEventType = savedStateHandle.getStateFlow<EventType?>(KEY_EVENT_TYPE, null)
    val selectedMonth = savedStateHandle.getStateFlow<Month?>(KEY_MONTH, null)
    val selectedSound = savedStateHandle.getStateFlow<String?>(KEY_SOUND, null)
    val selectedVenue = savedStateHandle.getStateFlow<String?>(KEY_VENUE, null)

    private val filterCriteria = combine(
        selectedMonth, selectedEventType, selectedSound, selectedVenue
    ) { month, type, sound, venue ->
        EventFilters(month, type, sound, venue)
    }.stateIn(viewModelScope, WhileSubscribed(5000L), EventFilters())
    // endregion

    // region Derived UI States (Computed Lists)
    val filteredEvents: StateFlow<List<Event>?> = combine(
        events,
        searchTextFieldValue,
        filterCriteria
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
    }.distinctUntilChanged()
     .stateIn(viewModelScope, WhileSubscribed(5000L), null)

    val bookmarks: StateFlow<List<Event>?> = combine(currentAppUser, events) { user, eventsList ->
        if (user == null || eventsList == null) return@combine null

        val allActiveEventIds = eventsList.map { it.id }.toSet()
        val userBookmarkIds = user.bookmarkIDs.toSet()

        val expiredIds = userBookmarkIds.filter { it !in allActiveEventIds }
        if (expiredIds.isNotEmpty()) {
            purgeExpiredBookmarks(expiredIds)
        }

        eventsList.filter { it.id in userBookmarkIds }
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
    }.distinctUntilChanged()
     .stateIn(viewModelScope, WhileSubscribed(5000L), null)

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
    // endregion

    // region Navigation Actions
    private val _scrollToEventId = MutableStateFlow<String?>(null)
    val scrollToEventId = _scrollToEventId.asStateFlow()

    fun setScrollTarget(eventId: String?) { _scrollToEventId.value = eventId }
    fun clearScrollTarget() { _scrollToEventId.value = null }
    // endregion

    // region User Input Actions (Search & Filters)
    fun updateSearchText(newValue: TextFieldValue) {
        _searchTextFieldValue.value = newValue
        savedStateHandle[KEY_SEARCH_TEXT] = newValue.text
        if (newValue.text.isNotEmpty()) clearScrollTarget()
    }

    fun updateEventType(type: EventType?) = toggleFilter(KEY_EVENT_TYPE, selectedEventType.value, type)
    fun updateMonth(month: Month?) = toggleFilter(KEY_MONTH, selectedMonth.value, month)
    fun updateSound(sound: String?) = toggleFilter(KEY_SOUND, selectedSound.value, sound)
    fun updateVenue(venue: String?) = toggleFilter(KEY_VENUE, selectedVenue.value, venue)

    fun clearAllFilters() {
        listOf(KEY_EVENT_TYPE, KEY_MONTH, KEY_SOUND, KEY_VENUE).forEach { key ->
            savedStateHandle[key] = null
        }
    }

    private fun <T> toggleFilter(key: String, currentValue: T?, newValue: T?) {
        savedStateHandle[key] = if (currentValue == newValue) null else newValue
    }
    // endregion

    // region Private Data Operations (Loading & Connectivity)
    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .onEach { isConnected ->
                if (isConnected && _events.value == null) {
                    loadEvents()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                _events.value = eventRepo.getProcessedEvents()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading events from repository.", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    // endregion

    // region Bookmark & Reminder Actions
    fun addBookmarkId(bookmarkId: String) {
        val event = _events.value?.find { it.id == bookmarkId } ?: return

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
        val event = _events.value?.find { it.id == bookmarkId } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.removeBookmarkIds(listOf(bookmarkId))

                if (currentAppUser.value?.settings?.notificationsEnabled == true) {
                    reminderScheduler.cancelEventReminders(event)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error removing bookmark ID: $bookmarkId.", ex)
            }
        }
    }

    private fun purgeExpiredBookmarks(expiredIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.removeBookmarkIds(expiredIds)
                Log.d(TAG, "Successfully purged ${expiredIds.size} bookmarks in one batch.")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to purge bookmarks batch", e)
            }
        }
    }
    // endregion

    // region Reminder & Permission Actions
    val hasNotificationPermission = permissionManager.hasNotificationPermission

    fun setupAbsenceReminder() = reminderScheduler.scheduleAbsenceReminder()

    fun addBookmarkReminder(event: Event) {
        listOf(
            EventReminderType.ONE_WEEK_BEFORE,
            EventReminderType.ONE_DAY_BEFORE,
            EventReminderType.ONE_HOUR_BEFORE
        ).forEach { type ->
            reminderScheduler.scheduleEventReminder(event, type, event.imageURL)
        }
    }
    // endregion
}