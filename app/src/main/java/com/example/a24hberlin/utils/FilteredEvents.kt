package com.example.a24hberlin.utils

import androidx.compose.ui.text.input.TextFieldValue
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.model.Event

fun filteredEvents(
    events: List<Event>,
    searchText: TextFieldValue,
    selectedMonth: Month? = null,
    selectedEventType: EventType? = null,
    selectedSound: String? = null,
    selectedVenue: String? = null
): List<Event> {
    val now = java.time.LocalDate.now()
    val currentYear = now.year
    val currentMonthValue = now.monthValue

    return events.filter { event ->
        val monthMatches = selectedMonth?.let { selMonth ->
            val targetYear = if (selMonth.value < currentMonthValue) {
                currentYear + 1
            } else {
                currentYear
            }

            event.start.month.value == selMonth.value && event.start.year == targetYear
        } ?: true

        val eventTypeMatches = selectedEventType == null ||
                event.eventType?.values.orEmpty().any {
                    it.cleanToAnnotatedString().text == selectedEventType.label
                }

        val soundMatches = selectedSound == null ||
                event.sounds?.values.orEmpty().any {
                    it == selectedSound
                }

        val venueMatches = selectedVenue == null ||
                event.locationName?.contains(
                    selectedVenue,
                    ignoreCase = true
                ) == true

        val textMatches = searchText.text.isEmpty() ||
                event.name.contains(
                    searchText.text,
                    ignoreCase = true
                )

        monthMatches && eventTypeMatches && soundMatches && venueMatches && textMatches
    }
}