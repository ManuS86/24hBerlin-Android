package com.example.a24hberlin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.data.model.Event

@Composable
fun filteredEvents(
    events: List<Event>,
    selectedMonth: Month?,
    selectedEventType: EventType?,
    selectedSound: Sound?,
    selectedVenue: String?,
    searchText: TextFieldValue
): List<Event> {
    return remember(
        events,
        selectedMonth,
        selectedEventType,
        selectedSound,
        selectedVenue,
        searchText
    ) {
        events.filter { event ->
            val monthMatches = selectedMonth?.let {
                val currentYear = java.time.Year.now().value

                event.start.month.value == selectedMonth.value && event.start.year == currentYear
            } ?: true

            val eventTypeMatches = selectedEventType?.let { selectedEventType ->
                event.eventType?.values.orEmpty().any { eventTypeValue ->
                    eventTypeValue == selectedEventType.label
                }
            } ?: true

            val soundMatches = selectedSound?.let { selectedSound ->
                event.sounds?.values.orEmpty().any { soundValue ->
                    soundValue == selectedSound.label
                }
            } ?: true

            val venueMatches = selectedVenue?.let { selectedVenue ->
                event.locationName?.lowercase()?.contains(selectedVenue.lowercase()) ?: false
            } ?: true

            val textMatches = event.name.lowercase().contains(
                searchText.text.lowercase()
            ).takeUnless {
                searchText.text.isEmpty()
            } ?: true

            monthMatches && eventTypeMatches && soundMatches && venueMatches && textMatches
        }
    }
}