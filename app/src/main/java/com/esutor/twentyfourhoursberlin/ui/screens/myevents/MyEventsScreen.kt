package com.esutor.twentyfourhoursberlin.ui.screens.myevents

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
import com.esutor.twentyfourhoursberlin.data.model.Event
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
fun MyEventsScreen(
    eventVM: EventViewModel,
    connectivityVM: ConnectivityViewModel
) {
    // --- State Observation ---
    val bookmarks by eventVM.filteredBookmarks.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isConnected.collectAsStateWithLifecycle()

    // Filter & Search states used as keys for Scroll-to-Top
    val searchText by eventVM.searchTextFieldValue.collectAsStateWithLifecycle()
    val selectedType by eventVM.selectedEventType.collectAsStateWithLifecycle()
    val selectedMonth by eventVM.selectedMonth.collectAsStateWithLifecycle()
    val selectedSound by eventVM.selectedSound.collectAsStateWithLifecycle()
    val selectedVenue by eventVM.selectedVenue.collectAsStateWithLifecycle()

    // --- UI State ---
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // --- Side Effects ---
    LaunchedEffect(
        searchText.text,
        selectedType,
        selectedMonth,
        selectedSound,
        selectedVenue
    ) {
        if (!bookmarks.isNullOrEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    // --- Layout ---
    Column(Modifier.fillMaxSize()) {
        when (bookmarks) {
            null -> { /* Loading/Silent State */ }

            emptyList<Event>() -> {
                if (isNetworkAvailable) NoEventsState() else OfflineState()
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(horizontal = regularPadding, vertical = halfPadding),
                    verticalArrangement = spacedBy(halfPadding)
                ) {
                    itemsIndexed(
                        items = bookmarks!!,
                        key = { _, bookmark -> bookmark.id }
                    ) { index, event ->
                        EventItem(
                            event = event,
                            eventVM = eventVM,
                            onCollapse = {
                                scope.launch {
                                    delay(50)
                                    listState.animateScrollToItem(
                                        index = index,
                                        scrollOffset = -30
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}