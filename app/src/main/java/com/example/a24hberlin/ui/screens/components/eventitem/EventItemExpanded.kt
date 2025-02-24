package com.example.a24hberlin.ui.screens.components.eventitem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.buttons.FavoriteButton
import com.example.a24hberlin.ui.screens.components.eventdetailitem.EventDetailItem
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Categories
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Header
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.ImageAndDate
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Location
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Time
import com.example.a24hberlin.ui.theme.ArtAndCulture
import com.example.a24hberlin.ui.theme.Concert
import com.example.a24hberlin.ui.theme.Party
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun EventItemExpanded(event: Event) {
    var showDetail by remember { mutableStateOf(false) }
    val eventColor = when {
        event.eventType?.values?.contains("Konzert") ?: true -> Concert
        event.eventType?.values?.contains("Party") ?: false -> Party
        else -> ArtAndCulture
    }

    Column(
        Modifier
            .background(Color.White)
            .clip(RoundedCornerShape(mediumRounding))
            .border(BorderStroke(0.5.dp, Color.Black), RoundedCornerShape(mediumRounding))
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(eventColor)
                    .padding(regularPadding)
                    .clickable {
                        showDetail = !showDetail
                    }
            ) {
                ImageAndDate(
                    event.imageURL,
                    event.start,
                    event.end
                )

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(mediumPadding)
                ) {
                    Header(
                        event.name,
                        event.permalink,
                        event.subtitle
                    )

                    Categories(
                        event.eventType,
                        event.sounds
                    )

                    Time(
                        event.start,
                        event.end
                    )

                    Location(
                        event.locationName,
                        event.address
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FavoriteButton(event)
                    }
                }
            }
        }
        EventDetailItem(event, showDetail) { showDetail = !showDetail }
    }
}