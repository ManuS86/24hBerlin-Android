package com.example.a24hberlin.ui.screens.myevents

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.screens.components.utilityelements.NoEventsFoundState
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun MyEventsScreen(
    searchText: TextFieldValue,
    connectivityVM: ConnectivityViewModel = viewModel(),
    eventVM: EventViewModel = viewModel()
) {
    val bookmarks by eventVM.bookmarks.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isNetworkAvailable.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val filteredEvents = remember(bookmarks, searchText.text) {
        filteredEvents(
            events = bookmarks,
            searchText = searchText
        )
    }

    LaunchedEffect(searchText.text) {
        if (filteredEvents.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column(Modifier.fillMaxSize()) {
        if (isNetworkAvailable) {
            if (filteredEvents.isEmpty()) {
                NoEventsFoundState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = regularPadding),
                    verticalArrangement = spacedBy(halfPadding),
                    state = listState,
                    contentPadding = PaddingValues(top = halfPadding, bottom = halfPadding)
                ) {
                    items(
                        items = filteredEvents,
                        key = { bookmark -> bookmark.id }
                    ) { bookmark ->
                        EventItem(bookmark)
                    }
                }
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.WifiOff,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(120.dp),
                tint = Gray
            )
        }
    }
}