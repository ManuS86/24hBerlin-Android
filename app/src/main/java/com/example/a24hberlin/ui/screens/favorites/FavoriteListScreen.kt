package com.example.a24hberlin.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun FavoritesScreen(
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: String?,
    selectedVenue: String?
) {
    val connectivityVM: ConnectivityViewModel = viewModel()
    val eventVM: EventViewModel = viewModel()

    val favorites by eventVM.favorites.collectAsStateWithLifecycle()
    val isNetworkAvailable by connectivityVM.isNetworkAvailable.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val filteredFavorites = filteredEvents(
        events = favorites,
        selectedMonth = selectedMonth,
        selectedEventType = selectedEventType,
        selectedSound = selectedSound,
        selectedVenue = selectedVenue,
        searchText = searchText
    )

    Column(Modifier.fillMaxSize()) {
        if (isNetworkAvailable) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = regularPadding),
                verticalArrangement = Arrangement.spacedBy(mediumPadding),
                state = listState,
                contentPadding = PaddingValues(top = mediumPadding, bottom = mediumPadding)
            ) {
                items(
                    items = filteredFavorites,
                    key = { favorite -> favorite.id }
                ) { favorite ->
                    EventItem(favorite)
                }
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.WifiOff,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(120.dp),
                tint = Color.Gray
            )
        }
    }
}