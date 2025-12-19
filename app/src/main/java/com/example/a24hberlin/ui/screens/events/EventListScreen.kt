package com.example.a24hberlin.ui.screens.events

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.screens.components.utilityelements.NoEventsFoundState
import com.example.a24hberlin.ui.screens.components.utilityelements.OfflineState
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun EventsScreen(
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: String?,
    selectedVenue: String?,
    connectivityVM: ConnectivityViewModel = viewModel(),
    eventVM: EventViewModel = viewModel()
) {
    val events by eventVM.events.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isNetworkAvailable.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val filteredEvents = remember(
        events, selectedMonth, selectedEventType,
        selectedSound, selectedVenue, searchText.text
    ) {
        filteredEvents(
            events = events,
            selectedMonth = selectedMonth,
            selectedEventType = selectedEventType,
            selectedSound = selectedSound,
            selectedVenue = selectedVenue,
            searchText = searchText
        )
    }

    LaunchedEffect(filteredEvents.size) {
        if (filteredEvents.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column {
        if (filteredEvents.isEmpty()) {
            if (isNetworkAvailable) {
                NoEventsFoundState()
            } else {
                OfflineState()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = regularPadding),
                verticalArrangement = spacedBy(halfPadding),
                state = listState,
                contentPadding = PaddingValues(top = halfPadding, bottom = halfPadding)
            ) {
                items(
                    items = filteredEvents,
                    key = { event -> event.id }
                ) { event ->
                    EventItem(event)
                }
            }
        }
    }
}