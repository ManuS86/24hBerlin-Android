package com.esutor.twentyfourhoursberlin.ui.screens.myevents

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
import kotlinx.coroutines.delay

@Composable
fun MyEventsScreen(
    eventVM: EventViewModel,
    connectivityVM: ConnectivityViewModel
) {
    // --- State Observation ---
    val bookmarks by eventVM.filteredBookmarks.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isConnected.collectAsStateWithLifecycle()
    val targetId by eventVM.scrollToEventId.collectAsStateWithLifecycle()

    // --- UI State ---
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // --- Side Effects ---
    LaunchedEffect(targetId, bookmarks) {
        val currentTarget = targetId
        val currentBookmarks = bookmarks

        if (currentTarget != null && !currentBookmarks.isNullOrEmpty()) {
            val index = currentBookmarks.indexOfFirst { it.id == currentTarget }
            if (index != -1) {
                delay(300)
                listState.animateScrollToItem(index)
                eventVM.clearScrollTarget()
            }
        }
    }

    LaunchedEffect(bookmarks) {
        if (targetId == null && !bookmarks.isNullOrEmpty() && listState.firstVisibleItemIndex > 0) {
            listState.scrollToItem(0)
        }
    }

    // --- Layout ---
    Box(Modifier.fillMaxSize()) {
        when {
            // 1. DATA PRESENT: Show list immediately
            bookmarks != null -> {
                EventList(
                    events = bookmarks,
                    listState = listState,
                    scope = scope,
                    eventVM = eventVM
                )

                if (bookmarks!!.isEmpty()) NoEventsState()
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