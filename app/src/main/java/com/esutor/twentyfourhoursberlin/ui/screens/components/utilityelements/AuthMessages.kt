package com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.res.stringResource
import com.esutor.twentyfourhoursberlin.ui.theme.errorPadding

@Composable
fun AuthMessages(
    confirmationMessageResId: Int?,
    errorMessageResId: Int? = null,
    firebaseError: String?
) {
    val (message, color) = when {
        confirmationMessageResId != null -> stringResource(confirmationMessageResId) to Green
        errorMessageResId != null -> stringResource(errorMessageResId) to Red
        firebaseError != null -> firebaseError to Red
        else -> null to Unspecified
    }

    message?.let {
        Text(
            text = it,
            modifier = Modifier.padding(top = errorPadding),
            color = color,
            style = typography.bodyMedium
        )
    }
}