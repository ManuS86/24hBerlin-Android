package com.example.a24hberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.theme.mediumRounding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapCard(event: Event) {
    val lat = event.lat ?: 52.5200 // Default to Berlin if null
    val lon = event.long ?: 13.4050

    val venue = remember(lat, lon) { LatLng(lat, lon) }
    val venueMarkerState = rememberMarkerState(position = venue)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(venue, 14f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(mediumRounding)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = venueMarkerState,
                title = event.name
            )
        }
    }
}