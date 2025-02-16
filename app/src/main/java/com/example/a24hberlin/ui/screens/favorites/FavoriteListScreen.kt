package com.example.a24hberlin.ui.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.screens.components.utilitybars.FilterBar
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun FavoritesScreen(searchText: TextFieldValue) {
    val eventVM: EventViewModel = viewModel()
    val listState: LazyListState = rememberLazyListState()
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var selectedMonth by remember { mutableStateOf< Month?>(null) }
    var selectedSound by remember { mutableStateOf<Sound?>(null) }
    var selectedVenue by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        eventVM.loadEvents()
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.fillMaxSize()) {
            Column(Modifier.background(Color.Black)) {
                FilterBar(
                    selectedEventType,
                    { selectedEventType = it },
                    selectedMonth,
                    { selectedMonth = it },
                    selectedSound,
                    { selectedSound = it },
                    selectedVenue,
                    { selectedVenue = it },
                    eventVM.uniqueLocations
                )
            }

            LazyColumn(
                Modifier
                    .padding(horizontal = regularPadding),
                verticalArrangement = Arrangement.spacedBy(mediumPadding),
                state = listState,
                contentPadding = PaddingValues(top = mediumPadding, bottom = mediumPadding)
            ) {
                items(
                    items = eventVM.favorites ?: emptyList(),
                    key = { favorite -> favorite.id }
                ) { favorite ->
                    EventItem(favorite)
                }
            }
        }
    }
}