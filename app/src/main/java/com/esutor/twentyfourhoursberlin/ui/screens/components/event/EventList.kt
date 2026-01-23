package com.esutor.twentyfourhoursberlin.ui.screens.components.event

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.esutor.twentyfourhoursberlin.data.models.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.EventItem
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EventList(
    events: List<Event>?,
    listState: LazyListState,
    eventVM: EventViewModel,
    targetId: String? = null
) {
    val scope = rememberCoroutineScope()

    val isAtBottom by remember {
        derivedStateOf {
            val lastItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(events) {
        if (targetId == null &&
            !events.isNullOrEmpty() &&
            listState.firstVisibleItemIndex > 0
        ) {
            listState.scrollToItem(0)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            horizontal = standardPadding,
            vertical = smallPadding
        ),
        verticalArrangement = spacedBy(smallPadding)
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
                    if (!isAtBottom) {
                        scope.launch {
                            delay(50)

                            listState.animateScrollToItem(index, -30)
                        }
                    }
                }
            )
        }
    }
}