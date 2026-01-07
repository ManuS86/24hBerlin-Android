package com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.esutor.twentyfourhoursberlin.ui.theme.SuccessGreen

@Composable
fun AuthMessages(
    confirmationMessageResId: Int? = null,
    errorMessageResId: Int? = null,
    firebaseError: String?
) {
    val (message, color) = when {
        confirmationMessageResId != null -> stringResource(confirmationMessageResId) to SuccessGreen
        errorMessageResId != null -> stringResource(errorMessageResId) to Red
        firebaseError != null -> firebaseError to Red
        else -> null to Unspecified
    }

    message?.let {
        Text(
            text = it,
            color = color,
            textAlign = Center,
            style = typography.bodyMedium
        )
    }
}