package com.example.a24hberlin.ui.screens.components.eventitem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.eventdetailitem.EventDetailItem
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Categories
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Header
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.ImageAndDate
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Location
import com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables.Time
import com.example.a24hberlin.utils.regularPadding

@Composable
fun EventDetailItem(event: Event) {
    SelectionContainer {
        Column {
            Card {
                Row(Modifier.padding(regularPadding)) {
                    ImageAndDate(
                        event.imageURL,
                        event.start,
                        event.end
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
                            event.start,
                            event.end
                        )
                        Location(
                            event.locationName,
                            event.address
                        )
                    }
                }
            }
            EventDetailItem()
        }
    }
}