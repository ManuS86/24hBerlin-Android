package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop

@Composable
fun AuthTextButton(
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    TextButton(
        onClick = onClick,
        modifier = Modifier.expressivePop(interactionSource, PopSpeed.Fast),
        interactionSource = interactionSource
    ) {
        Text(
            text = label,
            fontWeight = Bold,
            color = Black,
            style = typography.bodyLarge
        )
    }
}