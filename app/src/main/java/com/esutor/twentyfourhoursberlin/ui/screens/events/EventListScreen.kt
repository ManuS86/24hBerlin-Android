package com.esutor.twentyfourhoursberlin.ui.screens.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
    val scope = rememberCoroutineScope()

    // --- Side Effects ---
    LaunchedEffect(events) {
        if (!events.isNullOrEmpty() && listState.firstVisibleItemIndex > 0) {
            listState.scrollToItem(0)
        }
    }

    // --- Layout ---
    Box(Modifier.fillMaxSize()) {
        when {
            // 1. DATA PRESENT: Show list immediately
            events != null -> {
                EventList(
                    events = events,
                    listState = listState,
                    scope = scope,
                    eventVM = eventVM
                )

                if (events!!.isEmpty()) NoEventsState()
            }

            // 2. CONFIRMED OFFLINE: Only show if we are sure there's no net
            !isNetworkAvailable -> {
                OfflineState()
            }

            // 3. THE "SILENT" STATE: events is null BUT network is available (or unknown)
            else -> {
                // Do nothing. This shows the background/empty screen
            }
        }
    }
}