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
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.screens.components.utilityelements.NoEventsFoundState
import com.example.a24hberlin.ui.screens.components.utilityelements.OfflineState
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun EventsScreen(
    connectivityVM: ConnectivityViewModel,
    eventVM: EventViewModel
) {
    val events by eventVM.filteredEvents.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isNetworkAvailable.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(events.size) {
        if (events.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column {
        if (events.isEmpty()) {
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
                    items = events,
                    key = { event -> event.id }
                ) { event ->
                    EventItem(event)
                }
            }
        }
    }
}