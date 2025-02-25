package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.TextOffBlack
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.smallPadding

@Composable
fun DirectionsCard(onClick: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Details), RoundedCornerShape(mediumRounding)),
        shape = RoundedCornerShape(mediumRounding),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(regularPadding)
        ) {
            Icon(
                Icons.Default.Directions,
                contentDescription = null,
                Modifier.padding(end = mediumPadding),
                tint = TextOffBlack
            )

            Text(
                stringResource(R.string.directions),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextOffBlack
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                tint = Color.Gray,
                contentDescription = stringResource(R.string.further_information),
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = smallPadding)
                    .clickable {
                        onClick()
                    }
            )
        }
    }
}