package com.example.a24hberlin.ui.screens.clubmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun ClubMapScreen(searchText: TextFieldValue) {
    val eventVM: EventViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        eventVM.loadEvents()
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.fillMaxSize()) {
//            AndroidView()
        }
    }
}
