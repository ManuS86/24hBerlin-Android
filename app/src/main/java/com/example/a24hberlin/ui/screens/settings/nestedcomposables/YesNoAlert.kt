package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.a24hberlin.R

@Composable
fun YesNoAlert(
    title: String,
    body: String,
    onNo: () -> Unit,
    onYes: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onNo,
        title = { Text(title) },
        text = { Text(body) },
        containerColor = Color.White,

        confirmButton = {
            TextButton(onClick = onYes) {
                Text(
                    text = stringResource(R.string.yes),
                    color = Color.Red
                )
            }
        },

        dismissButton = {
            TextButton(onClick = onNo) {
                Text(stringResource(R.string.no))
            }
        }
    )
}