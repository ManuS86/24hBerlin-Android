package com.example.a24hberlin.ui.screens.clubmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents

@Composable
fun ClubMapScreen(
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: Sound?,
    selectedVenue: String?,
) {
    val eventVM: EventViewModel = viewModel()
    val events by eventVM.events.collectAsState()
    val filteredEvents = filteredEvents(
        events = events,
        selectedMonth = selectedMonth,
        selectedEventType = selectedEventType,
        selectedSound = selectedSound,
        selectedVenue = selectedVenue,
        searchText = searchText
    )

    LaunchedEffect(key1 = Unit) {
        eventVM.loadEvents()
    }

    Column(Modifier.fillMaxSize()) {
//            AndroidView()
    }
}
