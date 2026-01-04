package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables

import android.content.Intent
import android.content.Intent.ACTION_VIEW
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.core.net.toUri
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.OffWhite
import com.esutor.twentyfourhoursberlin.ui.theme.Party
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.utils.singleClick

@Composable
fun LearnMoreLinkCard(link: String?) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }


    link?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .expressivePop(interactionSource),
            shape = mediumRounding,
            colors = cardColors(OffWhite)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .singleClick(
                        interactionSource = interactionSource,
                        onClick = { context.startActivity(Intent(ACTION_VIEW, link.toUri())) }
                    )
                    .padding(standardPadding)
            ) {
                Icon(
                    imageVector = Default.Link,
                    contentDescription = null,
                    modifier = Modifier.padding(end = smallPadding),
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