package com.esutor.twentyfourhoursberlin.ui.screens.events

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.EventItem
import com.esutor.twentyfourhoursberlin.ui.screens.components.states.NoEventsState
import com.esutor.twentyfourhoursberlin.ui.screens.components.states.OfflineState
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    LaunchedEffect(events, events?.size) {
        if (!events.isNullOrEmpty() && listState.firstVisibleItemIndex > 0) {
            listState.scrollToItem(0)
        }
    }

    // --- Layout ---
    Column(Modifier.fillMaxSize()) {
        if (events != null && events!!.isEmpty()) {
            if (isNetworkAvailable) NoEventsState() else OfflineState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    horizontal = regularPadding,
                    vertical = halfPadding
                ),
                verticalArrangement = spacedBy(halfPadding)
            ) {
                itemsIndexed(
                    items = events ?: emptyList(),
                    key = { _, event -> event.id }
                ) { index, event ->
                    EventItem(
                        event = event,
                        eventVM = eventVM,
                        onCollapse = {
                            scope.launch {
                                delay(50)
                                listState.animateScrollToItem(index, -30)
                            }
                        }
                    )
                }
            }
        }
    }
}