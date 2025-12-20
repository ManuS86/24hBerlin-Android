package com.example.a24hberlin.ui.screens.myevents

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
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun MyEventsScreen(
    connectivityVM: ConnectivityViewModel,
    eventVM: EventViewModel
) {
    val bookmarks by eventVM.filteredBookmarks.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isNetworkAvailable.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(bookmarks) {
        if (bookmarks.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(Modifier.fillMaxSize()) {
        if (bookmarks.isEmpty()) {
            if (isNetworkAvailable) {
                NoEventsFoundState()
            } else {
                OfflineState()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = regularPadding),
                verticalArrangement = spacedBy(halfPadding),
                state = listState,
                contentPadding = PaddingValues(top = halfPadding, bottom = halfPadding)
            ) {
                items(
                    items = bookmarks,
                    key = { bookmark -> bookmark.id }
                ) { bookmark ->
                    EventItem(bookmark)
                }
            }
        }
    }
}