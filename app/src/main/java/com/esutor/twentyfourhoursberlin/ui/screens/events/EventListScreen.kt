package com.esutor.twentyfourhoursberlin.ui.screens.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.EventList
import com.esutor.twentyfourhoursberlin.ui.screens.components.states.NoEventsState
import com.esutor.twentyfourhoursberlin.ui.screens.components.states.OfflineState
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel

@Composable
fun EventsScreen(
    eventVM: EventViewModel,
    connectivityVM: ConnectivityViewModel
) {
    // --- State Observation ---
    val events by eventVM.filteredEvents.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isConnected.collectAsStateWithLifecycle()

    // --- UI State ---
    val listState = rememberLazyListState()

    // --- Layout ---
    Box(Modifier.fillMaxSize()) {
        when {
            !events.isNullOrEmpty() -> {
                EventList(events, listState, eventVM)
            }

            events?.isEmpty() == true -> {
                NoEventsState()
            }

            !isNetworkAvailable -> {
                OfflineState()
            }

            else -> { /* Silent Loading State */ }
        }
    }
}