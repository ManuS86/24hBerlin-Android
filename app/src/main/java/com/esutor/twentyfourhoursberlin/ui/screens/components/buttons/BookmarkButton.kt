package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.roundRipple
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel

@Composable
fun BookmarkButton(
    event: Event,
    eventVM: EventViewModel
) {
    val currentAppUser by eventVM.currentAppUser.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }

    val isBookmarked = currentAppUser?.bookmarkIDs?.contains(event.id) == true
    val icon = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd
    val label = stringResource(if (isBookmarked) R.string.remove else R.string.bookmark)

    Icon(
        imageVector = icon,
        contentDescription = label,
        modifier = Modifier
            .size(28.dp)
            .expressivePop(interactionSource, PopSpeed.Fast)
            .clickable(
                interactionSource = interactionSource,
                indication = roundRipple,
                role = Role.Button,
                onClick = {
                    if (isBookmarked) eventVM.removeBookmarkId(event.id)
                    else eventVM.addBookmarkId(event.id)
                }
            )
    )
}