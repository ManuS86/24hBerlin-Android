package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.theme.detailImageSize
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding

@Composable
fun ImageCard(imageURL: String?) {
    val context = LocalContext.current

    imageURL?.let { url ->
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build(),
            placeholder = rememberVectorPainter(image = Default.Image),
            error = rememberVectorPainter(image = Default.BrokenImage),
            contentDescription = stringResource(R.string.event_image),
            contentScale = Crop,
            modifier = Modifier
                .height(detailImageSize)
                .clip(mediumRounding)
        )
    }
}