package com.example.a24hberlin.ui.screens.components.buttons

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.a24hberlin.R

@Composable
fun ShareButton(context: Context, permalink: String) {
    Icon(
        imageVector = Icons.Default.Share,
        contentDescription = stringResource(R.string.share),
        Modifier.clickable {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, permalink)
            }
            context.startActivity(Intent.createChooser(intent, R.string.share_link.toString()))
        }
    )
}