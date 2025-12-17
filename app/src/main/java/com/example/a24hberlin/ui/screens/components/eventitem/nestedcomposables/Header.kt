package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.a24hberlin.ui.screens.components.buttons.ShareButton
import com.example.a24hberlin.utils.cleanToAnnotatedString
import com.example.a24hberlin.utils.mediumPadding

@Composable
fun Header(
    name: String,
    permalink: String,
    subtitle: String?
) {
    val context = LocalContext.current

    val cleanedName = remember(name) {
        name.cleanToAnnotatedString().text.uppercase()
    }

    Column(verticalArrangement = Arrangement.spacedBy(mediumPadding)) {
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = cleanedName,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            ShareButton(
                context = context,
                permalink = permalink
            )
        }

        subtitle?.let {
            val cleanedSubtitle = remember(it) {
                it.cleanToAnnotatedString().text.uppercase()
            }
            Text(
                text = cleanedSubtitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}