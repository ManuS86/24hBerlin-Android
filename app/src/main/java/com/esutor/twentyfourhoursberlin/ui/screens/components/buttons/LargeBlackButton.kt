package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.circle
import com.esutor.twentyfourhoursberlin.ui.theme.largePadding
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.utils.rememberSingleClick

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LargeBlackButton(
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val singleClickAction = rememberSingleClick { onClick() }

    Button(
        onClick = singleClickAction,
        modifier = Modifier
            .padding(top = largePadding)
            .fillMaxWidth()
            .expressivePop(interactionSource, useTapBuffer = false),
        shape = circle,
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        ),
        interactionSource = interactionSource
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(smallPadding),
            fontWeight = SemiBold,
            style = typography.bodyLarge
        )
    }
}