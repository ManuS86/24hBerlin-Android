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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.event.item.EventItem
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
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
import com.google.maps.android.compose.rememberMarkerState

private const val MAP_DEFAULT_ZOOM = 9.5f
private const val ZOOM_ANIM_MS = 800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubMapScreen(eventVM: EventViewModel) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val sheetState = rememberModalBottomSheetState()
    val events by eventVM.filteredEvents.collectAsStateWithLifecycle()

    var selectedEvent: Event? by remember { mutableStateOf(null) }
    var iconCache by remember { mutableStateOf(mapOf<Int, BitmapDescriptor>()) }

    val berlinCenter = LatLng(52.5200, 13.4050)

    val cameraPosition = LatLng(52.5100, 13.3400)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPosition, MAP_DEFAULT_ZOOM)
    }

    var isInitialLoad by remember { mutableStateOf(true) }

    LaunchedEffect(events) {
        MapsInitializer.initialize(context)
        val newCache = iconCache.toMutableMap()
        var cacheUpdated = false

        events.forEach { event ->
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
            return@LaunchedEffect
        }

        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(cameraPosition, MAP_DEFAULT_ZOOM),
            durationMs = ZOOM_ANIM_MS
        )
    }

    DisposableEffect(Unit) {
        onDispose { selectedEvent = null }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        events.reversed().forEachIndexed { index, event ->
            val baseLat = event.lat ?: berlinCenter.latitude
            val baseLng = event.long ?: berlinCenter.longitude

            val finalLatLng = remember(event.id, index, event.name, baseLat, baseLng) {
                val shift = 0.0001
                val nameHash = event.name.hashCode()

                val latOffset = ((nameHash + index) % 5) * shift
                val lngOffset = ((nameHash + index) % 5) * shift

                LatLng(baseLat + latOffset, baseLng + lngOffset)
            }

            key("${event.id}_$index") {
                Marker(
                    state = rememberMarkerState(position = finalLatLng),
                    anchor = Offset(0.5f, 0.9f),
                    icon = iconCache[event.getMarkerResourceId()],
                    title = event.name,
                    snippet = if (event.lat == null) "Location approximate" else null,
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
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

        @Suppress("AssignedValueIsNeverRead")
        ModalBottomSheet(
            onDismissRequest = { selectedEvent = null },
            containerColor = White,
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .padding(horizontal = regularPadding, vertical = halfPadding)
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
