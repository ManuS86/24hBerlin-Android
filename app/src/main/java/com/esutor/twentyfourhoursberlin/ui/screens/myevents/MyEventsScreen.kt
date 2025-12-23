package com.esutor.twentyfourhoursberlin.ui.screens.myevents

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
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.EventItem
import com.esutor.twentyfourhoursberlin.ui.screens.components.states.NoEventsFoundState
import com.esutor.twentyfourhoursberlin.ui.screens.components.states.OfflineState
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel

@Composable
fun MyEventsScreen(
    connectivityVM: ConnectivityViewModel,
    eventVM: EventViewModel
) {
    val bookmarks by eventVM.filteredBookmarks.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isConnected.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(bookmarks) {
        if (!bookmarks.isNullOrEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(Modifier.fillMaxSize()) {
        when (bookmarks){
            null -> {}

            emptyList<Event>() -> {
                if (isNetworkAvailable) {
                    NoEventsFoundState()
                } else {
                    OfflineState()
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = regularPadding),
                    verticalArrangement = spacedBy(halfPadding),
                    state = listState,
                    contentPadding = PaddingValues(top = halfPadding, bottom = halfPadding)
                ) {
                    items(
                        items = bookmarks!!,
                        key = { bookmark -> bookmark.id }
                    ) { bookmark ->
                        EventItem(bookmark, eventVM)
                    }
                }
            }
        }
    }
}