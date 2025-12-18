package com.example.a24hberlin.ui.screens.components.textfields

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.a24hberlin.ui.theme.errorPadding

@Composable
fun AuthMessages(
    confirmationMessageResId: Int?,
    errorMessageResId: Int? = null,
    firebaseError: String?
) {
    val (message, color) = when {
        confirmationMessageResId != null -> stringResource(confirmationMessageResId) to Color.Green
        errorMessageResId != null -> stringResource(errorMessageResId) to Color.Red
        firebaseError != null -> firebaseError to Color.Red
        else -> null to Color.Unspecified
    }

    message?.let {
        Text(
            text = it,
            modifier = Modifier.padding(top = errorPadding),
            color = color,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}