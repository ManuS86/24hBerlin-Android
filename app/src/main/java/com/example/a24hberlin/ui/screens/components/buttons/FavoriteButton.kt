package com.example.a24hberlin.ui.screens.components.buttons

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun FavoriteButton(event: Event) {
    val view = LocalView.current
    val eventVM: EventViewModel = viewModel()

    val currentAppUser by eventVM.currentAppUser.collectAsStateWithLifecycle()
    val isFavorite by remember {
        derivedStateOf { currentAppUser?.favoriteIDs?.contains(event.id) ?: false }
    }

    Icon(
        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
        contentDescription = if (isFavorite) stringResource(R.string.unfavorite) else stringResource(R.string.favorite),
        modifier = Modifier
            .size(28.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                role = Role.Button,
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)

                    if (!isFavorite) {
                        eventVM.addFavoriteID(favoriteID = event.id)
                    } else {
                        eventVM.removeFavoriteID(favoriteID = event.id)
                    }
                }
            )
    )
}