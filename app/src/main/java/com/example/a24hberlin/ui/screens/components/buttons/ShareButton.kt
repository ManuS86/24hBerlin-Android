package com.example.a24hberlin.ui.screens.components.buttons

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun ShareButton(context: Context, permalink: String) {
    IconButton(onClick = {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, permalink)
        }
        context.startActivity(Intent.createChooser(intent, "Share link"))
    }) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share"
        )
    }
}