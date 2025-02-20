package com.example.a24hberlin.ui.screens.clubmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun ClubMapScreen(searchText: TextFieldValue) {
    val eventVM: EventViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        eventVM.loadEvents()
    }

    Column(Modifier.fillMaxSize()) {
//            AndroidView()
    }
}
