package com.example.a24hberlin.ui.screens.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun FavoriteButton(event: Event) {
    val eventVM: EventViewModel = viewModel()

    val isFavorite by remember {
        derivedStateOf {
            eventVM.currentAppUser?.favoriteIDs?.contains(event.id) ?: false
        }
    }

    Icon(
        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
        contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
        Modifier.clickable {
            if (isFavorite) {
                eventVM.removeFavoriteID(favoriteID = event.id)
            } else {
                eventVM.addFavoriteID(favoriteID = event.id)
            }
        }
    )
}