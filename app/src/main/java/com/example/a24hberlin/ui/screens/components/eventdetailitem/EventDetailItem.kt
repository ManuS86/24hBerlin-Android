package com.example.a24hberlin.ui.screens.components.eventdetailitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.DetailCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.DirectionsCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.EntranceFeeCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.ImageCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.LearnmoreLinkCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.LocationCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.MapCard
import com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables.TimeCard
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.viewmodel.MapViewModel
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding

@Composable
fun EventDetailItem(event: Event, showDetail: Boolean, showDetailToggle: () -> Unit) {
    val mapVM: MapViewModel = viewModel()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(mediumPadding)
    ) {
        ImageCard(event.imageURL)
        DetailCard(event.details)
        TimeCard(event.start, event.end)
        EntranceFeeCard(event.entranceFee)
        LocationCard(event.locationName, event.address)
        LearnmoreLinkCard(event.learnmoreLink)

        event.lat?.let {
            event.long?.let {
                DirectionsCard { mapVM }
                MapCard()
            }
        }

        if (showDetail) {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(mediumRounding),
                colors = CardDefaults.cardColors(
                    containerColor = Details
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Decollapse",
                    tint = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(mediumPadding)
                        .clickable {
                            showDetailToggle()
                        }
                )
            }
        }
    }
}