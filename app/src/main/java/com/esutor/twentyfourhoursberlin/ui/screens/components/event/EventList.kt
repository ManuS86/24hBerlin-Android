package com.esutor.twentyfourhoursberlin.ui.screens.components.event

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.EventItem
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EventList(
    events: List<Event>?,
    listState: LazyListState,
    scope: CoroutineScope,
    eventVM: EventViewModel,
    targetId: String? = null
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            horizontal = regularPadding,
            vertical = halfPadding
        ),
        verticalArrangement = spacedBy(halfPadding)
    ) {
        itemsIndexed(
            items = events ?: emptyList(),
            key = { _, event -> event.id }
        ) { index, event ->
            EventItem(
                event = event,
                eventVM = eventVM,
                targetId = targetId,
                onCollapse = {
                    scope.launch {
                        delay(50)
                        listState.animateScrollToItem(index, -30)
                    }
                }
            )
        }
    }
}