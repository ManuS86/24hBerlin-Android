package com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.AuthTextButton

@Composable
fun AuthPrompt(
    textResId: Int,
    buttonLabelResId: Int,
    onClick: () -> Unit
) {
    Text(
        text = stringResource(textResId),
        color = Gray,
        textAlign = Center
    )
    AuthTextButton(stringResource(buttonLabelResId),onClick)
}