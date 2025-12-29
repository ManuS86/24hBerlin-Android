package com.esutor.twentyfourhoursberlin.ui.screens.components.event.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.BookmarkButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.EventDetailItem
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.nestedcomposables.Categories
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.nestedcomposables.Header
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.nestedcomposables.ImageAndDate
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.nestedcomposables.Location
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.nestedcomposables.Time
import com.esutor.twentyfourhoursberlin.ui.theme.TextOffBlack
import com.esutor.twentyfourhoursberlin.utils.getEventColor
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding
import com.esutor.twentyfourhoursberlin.ui.theme.microPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel

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
    eventVM: EventViewModel,
    isExpandable: Boolean = true,
    isInitiallyExpanded: Boolean = false,
    onCollapse: () -> Unit = {}
) {
    var showDetail by remember { mutableStateOf(isInitiallyExpanded) }
    val eventColor = event.getEventColor()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        Modifier
            .expressivePop(interactionSource, PopSpeed.Slow)
            .animateContentSize()
            .clip(mediumRounding)
            .border(
                BorderStroke(if (showDetail) 0.5.dp else 0.dp, TextOffBlack),
                mediumRounding
            )
            .background(White)
    ) {
        CompositionLocalProvider(LocalContentColor provides White) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(eventColor)
                    .run {
                        if (isExpandable) {
                            clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    if (showDetail) onCollapse()
                                    showDetail = !showDetail
                                }
                            )
                        } else this
                    }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = regularPadding)
                        .padding(horizontal = regularPadding)
                ) {
                    ImageAndDate(event.imageURL, event.start, event.end)

                    Column(
                        horizontalAlignment = Start,
                        verticalArrangement = spacedBy(halfPadding),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Header(event.name, event.permalink, event.subtitle)
                        Categories(event.eventType, event.sounds)
                        Time(event.start, event.end)
                        Location(event.locationName, event.address)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = regularPadding),
                            horizontalArrangement = End
                        ) {
                            BookmarkButton(event, eventVM)
                        }
                    }
                }

                if (isExpandable) {
                    Icon(
                        imageVector = if (showDetail) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .align(BottomCenter)
                            .padding(bottom = microPadding)
                    )
                }
            }
        }

        if (showDetail) {
            EventDetailItem(
                event = event,
                isExpandable = isExpandable,
                showDetailToggle = {
                    if (isExpandable) {
                        onCollapse()
                        showDetail = false
                    }
                }
            )
        }
    }
}