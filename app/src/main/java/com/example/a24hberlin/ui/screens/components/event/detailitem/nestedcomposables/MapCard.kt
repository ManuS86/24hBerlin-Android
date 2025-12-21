package com.example.a24hberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.theme.mapPreviewHeight
import com.example.a24hberlin.ui.theme.mediumRounding
import com.example.a24hberlin.utils.bitmapDescriptorFromVector
import com.example.a24hberlin.utils.getMarkerResourceId
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private const val MAP_DEFAULT_ZOOM = 14f

@Composable
fun MapCard(event: Event) {
    val context = LocalContext.current

    val lat = event.lat ?: 52.5200 // Default to Berlin if null
    val lon = event.long ?: 13.4050
    val venue = remember(lat, lon) { LatLng(lat, lon) }
    val markerResId = remember(event.id) { event.getMarkerResourceId() }

    var customIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    val venueMarkerState = rememberMarkerState(position = venue)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(venue, MAP_DEFAULT_ZOOM)
    }

    LaunchedEffect(markerResId) {
        MapsInitializer.initialize(context)
        customIcon = bitmapDescriptorFromVector(context, markerResId)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(mapPreviewHeight),
        shape = mediumRounding
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            if (customIcon != null) {
                Marker(
                    state = venueMarkerState,
                    anchor = Offset(0.5f, 0.9f),
                    title = event.name,
                    icon = customIcon
                )
            }
        }
    }
}