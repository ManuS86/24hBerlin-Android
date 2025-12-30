package com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.nestedcomposables

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.ShareButton
import com.esutor.twentyfourhoursberlin.utils.cleanToAnnotatedString
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding

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

    Column(verticalArrangement = spacedBy(smallPadding)) {
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = cleanedName,
                fontWeight = Black,
                style = typography.titleLarge,
                overflow = Ellipsis,
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
                style = typography.titleMedium,
                fontWeight = Medium
            )
        }
    }
}