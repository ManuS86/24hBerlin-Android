package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.TextOffBlack
import com.example.a24hberlin.utils.getEventColor
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.mediumRounding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun DirectionsCard(event: Event, onClick: () -> Unit) {
    val eventColor = event.getEventColor()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Details),
                RoundedCornerShape(mediumRounding)
            ),
        shape = RoundedCornerShape(mediumRounding),
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
                shape = RoundedCornerShape(100),
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