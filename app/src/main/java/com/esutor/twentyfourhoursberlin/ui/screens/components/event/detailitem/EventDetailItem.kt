package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import com.esutor.twentyfourhoursberlin.data.models.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.DetailCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.DirectionsCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.EntranceFeeCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.ImageCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.LearnMoreLinkCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.LocationCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.MapCard
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables.TimeCard
import com.esutor.twentyfourhoursberlin.ui.theme.OffWhite
import com.esutor.twentyfourhoursberlin.managers.ExternalMapNavigator
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding

/**
 * Displays the detailed information for an event, typically used inside an expandable container
 * or a modal sheet.
 *
 * @param event The event data model.
 * @param isExpandable Flag indicating if the parent container allows collapsing (used to show/hide the collapse button).
 * @param showDetailToggle Callback function to collapse the parent container.
 */
@Composable
fun EventDetailItem(event: Event, isExpandable: Boolean, showDetailToggle: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(smallPadding),
        verticalArrangement = spacedBy(smallPadding)
    ) {
        ImageCard(event.imageURL)
        DetailCard(event.details)
        TimeCard(event.start, event.end)
        EntranceFeeCard(event.entranceFee)
        LocationCard(event.locationName, event.address)
        LearnMoreLinkCard(event.learnmoreLink)

        event.lat?.let {
            event.long?.let {
                DirectionsCard(event) {
                    ExternalMapNavigator.navigateToGoogleMaps(
                        context,
                        event.lat,
                        event.long
                    )
                }
                MapCard(event)
            }
        }

        if (isExpandable) {
            val collapseInteractionSource = remember { MutableInteractionSource() }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .expressivePop(collapseInteractionSource),
                shape = mediumRounding,
                colors = cardColors(
                    containerColor = OffWhite
                )
            ) {
                Icon(
                    imageVector = Default.KeyboardArrowUp,
                    contentDescription = "Collapse",
                    tint = Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            role = Role.Button,
                            interactionSource = collapseInteractionSource,
                            onClick = showDetailToggle
                        )
                        .padding(smallPadding)
                )
            }
        }
    }
}