package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.theme.Details
import com.esutor.twentyfourhoursberlin.ui.theme.TextOffBlack
import com.esutor.twentyfourhoursberlin.ui.theme.circle
import com.esutor.twentyfourhoursberlin.utils.getEventColor
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Composable
fun DirectionsCard(event: Event, onClick: () -> Unit) {
    val eventColor = event.getEventColor()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Details),
                mediumRounding
            ),
        shape = mediumRounding,
        colors = cardColors(
            containerColor = White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(regularPadding),
            verticalAlignment = CenterVertically
        ) {
            Icon(
                imageVector = Default.Directions,
                contentDescription = null,
                modifier = Modifier.padding(end = halfPadding),
                tint = TextOffBlack
            )

            Text(
                text = stringResource(R.string.directions),
                style = typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = ExtraBold,
                color = TextOffBlack
            )

            Spacer(Modifier.weight(1f))

            Card(
                modifier = Modifier.size(40.dp),
                shape = circle,
                colors = cardColors(
                    containerColor = eventColor
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        tint = White,
                        contentDescription = stringResource(R.string.directions),
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(20.dp)
                            .clickable { onClick() }
                    )
                }
            }
        }
    }
}