package com.example.a24hberlin.ui.screens.components.eventitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun EventItemExpanded(event: Event) {
    val eventColor = when {
        event.eventType?.values?.contains("Konzert") ?: true -> Concert
        event.eventType?.values?.contains("Party") ?: false -> Party
        else -> ArtAndCulture
    }

    SelectionContainer {
        Card(
            Modifier
                .background(eventColor)
                .shadow(1.dp),
            shape = RoundedCornerShape(mediumRounding)
        ) {
            Row(Modifier.padding(regularPadding)) {
                ImageAndDate(
                    event.imageURL,
                    event.startTime,
                    event.endTime
                )

                Column(horizontalAlignment = Alignment.Start) {
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
                        event.startTime,
                        event.endTime
                    )
                    Location(
                        event.locationName,
                        event.address
                    )
                    FavoriteButton(event)
                }
            }
        }
        EventDetailItem()
    }
}