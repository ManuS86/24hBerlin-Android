package com.example.a24hberlin.ui.screens.components.buttons

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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun BookmarkButton(event: Event) {
    val haptic = LocalHapticFeedback.current
    val eventVM: EventViewModel = viewModel()

    val currentAppUser by eventVM.currentAppUser.collectAsStateWithLifecycle()
    val isBookmarked by remember {
        derivedStateOf { currentAppUser?.bookmarkIDs?.contains(event.id) ?: false }
    }

    Icon(
        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
        contentDescription = if (isBookmarked) stringResource(R.string.remove) else stringResource(R.string.bookmark),
        modifier = Modifier
            .size(28.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(TextHandleMove)

                    if (!isBookmarked) {
                        eventVM.addBookmarkId(bookmarkId = event.id)
                    } else {
                        eventVM.removeBookmarkId(bookmarkId = event.id)
                    }
                }
            )
    )
}