package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    name: String,
    permalink: String,
    subtitle: String?
) {
    val context = LocalContext.current

    Row {
        Text(
            name.uppercase(),
            fontWeight = FontWeight.Black,
            fontSize = 22.sp
        )
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            modifier = Modifier
                .clickable {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, permalink)
                }
                context.startActivity(Intent.createChooser(intent, "Share link"))
            }
        )
    }

    subtitle?.let {
        Text(
            subtitle.uppercase(),
            fontWeight = FontWeight.SemiBold
        )
    }
}