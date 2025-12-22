package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.core.net.toUri
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.theme.Details
import com.esutor.twentyfourhoursberlin.ui.theme.Party
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Composable
fun LearnMoreLinkCard(link: String?) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    link?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    role = Role.Button,
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        context.startActivity(Intent(ACTION_VIEW, link.toUri()))
                    }
                ),
            shape = mediumRounding,
            colors = cardColors(
                containerColor = Details
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(regularPadding)
            ) {
                Icon(
                    imageVector = Default.Link,
                    contentDescription = null,
                    modifier = Modifier.padding(end = halfPadding),
                    tint = Party
                )

                Text(
                    text = stringResource(R.string.further_information),
                    style = typography.titleMedium,
                    fontWeight = ExtraBold
                )
            }
        }
    }
}