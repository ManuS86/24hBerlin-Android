package com.esutor.twentyfourhoursberlin.ui.screens.clubmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.EventItem
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.utils.bitmapDescriptorFromVector
import com.esutor.twentyfourhoursberlin.utils.getMarkerResourceId
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

private const val MAP_DEFAULT_ZOOM = 9.5f
private const val ZOOM_ANIM_MS = 800
private val BERLIN_CENTER = LatLng(52.5200, 13.4050)
private const val SHIFT_AMOUNT = 0.00015

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubMapScreen(eventVM: EventViewModel) {
    val context = LocalContext.current
    val events by eventVM.filteredEvents.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var selectedEvent: Event? by remember { mutableStateOf(null) }
    var iconCache by remember { mutableStateOf(mapOf<Int, BitmapDescriptor>()) }

    val cameraPosition = LatLng(52.5100, 13.3400)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPosition, MAP_DEFAULT_ZOOM)
    }

    var isInitialLoad by remember { mutableStateOf(true) }

    val positionedEvents = remember(events) {
        val grouped = events?.groupBy {
            "${it.lat ?: BERLIN_CENTER.latitude},${it.long ?: BERLIN_CENTER.longitude}"
        }

        grouped?.flatMap { (_, eventList) ->
            eventList.mapIndexed { index, event ->
                val baseLat = event.lat ?: BERLIN_CENTER.latitude
                val baseLng = event.long ?: BERLIN_CENTER.longitude

                val multiplier = index.toDouble()

                val finalLatLng = LatLng(
                    baseLat + (multiplier * SHIFT_AMOUNT),
                    baseLng + (multiplier * SHIFT_AMOUNT)
                )
                event to finalLatLng
            }
        }
    }

    LaunchedEffect(events) {
        MapsInitializer.initialize(context)
        val newCache = iconCache.toMutableMap()
        var cacheUpdated = false

        events?.forEach { event ->
            val resId = event.getMarkerResourceId()
            if (!newCache.containsKey(resId)) {
                bitmapDescriptorFromVector(context, resId)?.let {
                    newCache[resId] = it
                    cacheUpdated = true
                }
            }
        }
        if (cacheUpdated) iconCache = newCache
    }

    @Suppress("AssignedValueIsNeverRead")
    LaunchedEffect(events) {
        if (isInitialLoad) {
            isInitialLoad = false
        } else {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(cameraPosition, MAP_DEFAULT_ZOOM),
                durationMs = ZOOM_ANIM_MS
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose { selectedEvent = null }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        positionedEvents?.forEach { (event, finalLatLng) ->
            key(event.id) {
                Marker(
                    state = rememberUpdatedMarkerState(position = finalLatLng),
                    icon = iconCache[event.getMarkerResourceId()],
                    title = event.name,
                    zIndex = if (selectedEvent?.id == event.id) 1f else 0f,
                    onClick = {
                        selectedEvent = event
                        true
                    }
                )
            }
        }
    }

    selectedEvent?.let { event ->
        LaunchedEffect(event.id) {
            sheetState.show()
        }

        ModalBottomSheet(
            onDismissRequest = { selectedEvent = null },
            containerColor = White,
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .padding(horizontal = standardPadding, vertical = smallPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                EventItem(
                    event = event,
                    eventVM = eventVM,
                    isExpandable = false,
                    isInitiallyExpanded = true
                )
            }
        }
    }
}
