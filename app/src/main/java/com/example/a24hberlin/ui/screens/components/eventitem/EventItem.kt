package com.example.a24hberlin.ui.screens.components.eventitem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.a24hberlin.ui.theme.TextOffBlack
import com.example.a24hberlin.utils.getEventColor
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding

/**
 * Single composable component to represent an Event item, supporting both collapsed/expandable
 * and permanently expanded states.
 *
 * @param event The data model for the event.
 * @param isExpandable If true, the item is clickable to toggle expansion, and the detail view
 * will show a close button. Defaults to true (list item mode).
 * @param isInitiallyExpanded If true, the detail view is shown on first composition.
 */
@Composable
fun EventItem(
    event: Event,
    isExpandable: Boolean = true,
    isInitiallyExpanded: Boolean = false
) {
    var showDetail by remember { mutableStateOf(isInitiallyExpanded) }
    val eventColor = event.getEventColor()

    val rowModifier = Modifier
        .fillMaxWidth()
        .background(eventColor)
        .run {
            if (isExpandable) {
                clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { showDetail = !showDetail }
                )
            } else {
                this
            }
        }
        .padding(regularPadding)

    Column(
        Modifier
            .clip(RoundedCornerShape(mediumRounding))
            .border(BorderStroke(0.5.dp, TextOffBlack), RoundedCornerShape(mediumRounding))
            .background(Color.White)
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Row(rowModifier) {
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FavoriteButton(event)
                    }
                }
            }
        }

        if (showDetail) {
            val onDetailClose: () -> Unit = {
                if (isExpandable) {
                    showDetail = false
                }
            }

            EventDetailItem(
                event = event,
                isExpandable = isExpandable,
                showDetailToggle = onDetailClose
            )
        }
    }
}