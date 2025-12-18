package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.detailImageSize
import com.example.a24hberlin.ui.theme.mediumRounding

@Composable
fun ImageCard(imageURL: String?) {
    imageURL?.let { url ->
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            placeholder = rememberVectorPainter(image = Icons.Default.Image),
            error = rememberVectorPainter(image = Icons.Default.BrokenImage),
            contentDescription = stringResource(R.string.event_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(detailImageSize)
                .clip(RoundedCornerShape(mediumRounding))
        )
    }
}