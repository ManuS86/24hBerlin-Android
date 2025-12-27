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

    // --- UI State ---
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // --- Side Effects ---
    LaunchedEffect(bookmarks, bookmarks?.size) {
        if (!bookmarks.isNullOrEmpty() && listState.firstVisibleItemIndex > 0) {
            listState.scrollToItem(0)
        }
    }

    // --- Layout ---
    Column(Modifier.fillMaxSize()) {
        if (bookmarks.isNullOrEmpty()) {
            if (isNetworkAvailable) NoEventsState() else OfflineState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(horizontal = regularPadding, vertical = halfPadding),
                verticalArrangement = spacedBy(halfPadding)
            ) {
                itemsIndexed(
                    items = bookmarks ?: emptyList(),
                    key = { _, bookmark -> bookmark.id }
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