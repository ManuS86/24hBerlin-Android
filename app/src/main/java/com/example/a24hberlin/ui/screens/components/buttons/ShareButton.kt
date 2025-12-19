package com.example.a24hberlin.ui.screens.components.buttons

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.rippleRadius

@Composable
fun ShareButton(context: Context, permalink: String) {
    val haptic = LocalHapticFeedback.current

    Icon(
        imageVector = Default.Share,
        contentDescription = stringResource(R.string.share),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = rippleRadius),
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    val intent = Intent(ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(EXTRA_TEXT, permalink)
                    }
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            R.string.share_link.toString()
                        )
                    )
                }
            )
    )
}