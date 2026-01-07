package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.utils.rememberSingleClick

@Composable
fun AuthTextButton(
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val singleClickAction = rememberSingleClick { onClick() }

    TextButton(
        onClick = singleClickAction,
        modifier = Modifier.expressivePop(interactionSource, PopSpeed.Fast),
        interactionSource = interactionSource
    ) {
        Text(
            text = label,
            fontWeight = Bold,
            color = colorScheme.onSurface,
            style = typography.bodyLarge
        )
    }
}