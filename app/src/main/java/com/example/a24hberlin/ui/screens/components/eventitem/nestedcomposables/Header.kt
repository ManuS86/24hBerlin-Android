package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.a24hberlin.ui.screens.components.buttons.ShareButton

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

        ShareButton(context, permalink)
    }

    subtitle?.let {
        Text(
            subtitle.uppercase(),
            fontWeight = FontWeight.SemiBold
        )
    }
}