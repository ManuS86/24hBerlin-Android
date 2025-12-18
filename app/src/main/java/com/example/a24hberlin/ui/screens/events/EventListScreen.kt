package com.example.a24hberlin.ui.screens.events

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.screens.components.utilityelements.NoEventsFoundState
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.example.a24hberlin.ui.theme.mediumPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun EventsScreen(
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: String?,
    selectedVenue: String?
) {
    val context = LocalContext.current
    val connectivityVM: ConnectivityViewModel = viewModel()
    val eventVM: EventViewModel = viewModel(viewModelStoreOwner = context as ComponentActivity)

    val events by eventVM.events.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isNetworkAvailable.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val filteredEvents = filteredEvents(
        events = events,
        selectedMonth = selectedMonth,
        selectedEventType = selectedEventType,
        selectedSound = selectedSound,
        selectedVenue = selectedVenue,
        searchText = searchText
    )

    LaunchedEffect(
        selectedMonth,
        selectedEventType,
        selectedSound,
        selectedVenue,
        searchText.text
    ) {
        if (filteredEvents.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column {
        if (isNetworkAvailable) {
            if (filteredEvents.isEmpty()) {
                NoEventsFoundState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = regularPadding),
                    verticalArrangement = spacedBy(mediumPadding),
                    state = listState,
                    contentPadding = PaddingValues(top = mediumPadding, bottom = mediumPadding)
                ) {
                    items(
                        items = filteredEvents,
                        key = { event -> event.id }
                    ) { event ->
                        EventItem(event, eventVM)
                    }
                }
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.WifiOff,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(120.dp),
                tint = Gray
            )
        }
    }
}