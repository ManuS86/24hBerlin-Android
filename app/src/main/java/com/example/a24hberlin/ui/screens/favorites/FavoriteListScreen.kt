package com.example.a24hberlin.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
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
    selectedVenue: String?,
) {
    val eventVM: EventViewModel = viewModel()
    val listState = rememberLazyListState()
    val favorites by eventVM.favorites.collectAsState()
    val filteredFavorites = filteredEvents(
        events = favorites,
        selectedMonth = selectedMonth,
        selectedEventType = selectedEventType,
        selectedSound = selectedSound,
        selectedVenue = selectedVenue,
        searchText = searchText
    )

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .padding(horizontal = regularPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            state = listState,
            contentPadding = PaddingValues(top = mediumPadding, bottom = mediumPadding)
        ) {
            items(
                filteredFavorites,
                { favorite -> favorite.id }
            ) { favorite ->
                EventItem(favorite)
            }
        }
    }
}