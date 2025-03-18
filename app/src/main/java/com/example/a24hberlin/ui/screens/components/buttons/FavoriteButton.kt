package com.example.a24hberlin.ui.screens.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun FavoriteButton(event: Event) {
    val eventVM: EventViewModel = viewModel()
    val currentAppUser by eventVM.currentAppUser.collectAsStateWithLifecycle()

    val isFavorite by remember {
        derivedStateOf { currentAppUser?.favoriteIDs?.contains(event.id) ?: false }
    }

    Icon(
        imageVector = if (isFavorite) ImageVector.vectorResource(R.drawable.calendar_added) else ImageVector.vectorResource(
            R.drawable.calendar_add
        ),
        contentDescription = if (isFavorite) stringResource(R.string.unfavorite) else stringResource(
            R.string.favorite
        ),
        Modifier
            .size(28.dp)
            .clickable {
                if (!isFavorite) {
                    eventVM.addFavoriteID(favoriteID = event.id)
                } else {
                    eventVM.removeFavoriteID(favoriteID = event.id)
                }
            }
    )
}