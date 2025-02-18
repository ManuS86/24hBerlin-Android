package com.example.a24hberlin.ui.screens.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun EventsScreen(searchText: TextFieldValue) {
    val eventVM: EventViewModel = viewModel()
    val listState = rememberLazyListState()


    LaunchedEffect(key1 = Unit) {
        eventVM.loadEvents()
    }

    Box {
        Image(
            painterResource(R.drawable.background),
            null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = regularPadding),
                verticalArrangement = Arrangement.spacedBy(mediumPadding),
                state = listState,
                contentPadding = PaddingValues(top = mediumPadding, bottom = mediumPadding)
            ) {
                items(
                    eventVM.events,
                    { event -> event.id }
                ) { event ->
                    EventItem(event)
                }
            }
        }
    }
}