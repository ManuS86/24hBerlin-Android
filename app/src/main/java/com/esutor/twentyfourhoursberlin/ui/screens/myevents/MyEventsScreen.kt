package com.esutor.twentyfourhoursberlin.ui.screens.myevents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

    // --- Side Effects ---
    LaunchedEffect(targetId, bookmarks) {
        val currentTarget = targetId
        val currentBookmarks = bookmarks

        if (currentTarget != null && !currentBookmarks.isNullOrEmpty()) {
            val index = currentBookmarks.indexOfFirst { it.id == currentTarget }
            if (index != -1) {
                delay(300)

                val offset = if (listState.canScrollForward) -30 else 0
                listState.animateScrollToItem(index, offset)

                delay(500)
                eventVM.clearScrollTarget()
            }
        }
    }

    // --- Layout ---
    Box(Modifier.fillMaxSize()) {
        when {
            !bookmarks.isNullOrEmpty() -> {
                EventList(bookmarks, listState, eventVM, targetId)
            }

            bookmarks?.isEmpty() == true -> {
                NoEventsState()
            }

            !isNetworkAvailable -> {
                OfflineState()
            }

            else -> { /* Silent Loading State */ }
        }
    }
}