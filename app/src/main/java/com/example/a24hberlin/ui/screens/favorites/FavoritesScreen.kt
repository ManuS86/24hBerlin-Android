package com.example.a24hberlin.ui.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.utilitybars.FilterBar
import com.example.a24hberlin.ui.screens.components.utilitybars.SearchBar
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun FavoritesScreen() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            Modifier.fillMaxSize()
        ) {
            Column(Modifier.background(Color.Black)) {
                SearchBar(
                    searchText
                ) { searchText = it }
                FilterBar()
            }
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = regularPadding)
                    .padding(vertical = mediumPadding)
            ) {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen()
}