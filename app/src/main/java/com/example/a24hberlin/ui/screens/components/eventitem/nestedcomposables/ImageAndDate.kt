package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.a24hberlin.R
import com.example.a24hberlin.utils.eventImageSize
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.smallPadding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ImageAndDate(
    imageURL: String?,
    start: LocalDateTime,
    end: LocalDateTime?
) {
    val locale = Locale.getDefault()

    Column(modifier = Modifier.padding(end = mediumPadding)) {
        imageURL?.let { imageURL ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageURL)
                    .crossfade(true)
                    .build(),
                placeholder = rememberVectorPainter(image = Icons.Default.Image),
                error = rememberVectorPainter(image = Icons.Default.BrokenImage),
                contentDescription = stringResource(R.string.event_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(eventImageSize)
                    .clip(RoundedCornerShape(mediumRounding))
                    .padding(end = smallPadding)
            )
        }

        Row(
            modifier = Modifier.size(eventImageSize),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.padding(start = smallPadding),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    start.format(
                        DateTimeFormatter.ofPattern("EEE").withLocale(locale) // Weekday
                    ).substring(0, 2).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(y = 3.dp)
                )

                Text(
                    start.format(
                        DateTimeFormatter.ofPattern("dd").withLocale(locale) // Day
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )

                Text(
                    start.format(
                        DateTimeFormatter.ofPattern("MMM").withLocale(locale) // Month
                    ).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }

            end?.let {
                if (
                    end.format(DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(locale))
                    != start.format(DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(locale))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "-",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .padding(top = mediumPadding)
                                .padding(horizontal = 2.dp)
                        )

                        Column {
                            Text(
                                end.format(
                                    DateTimeFormatter.ofPattern("EEE")
                                        .withLocale(locale) // Weekday
                                ).substring(0, 2).uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                            )

                            Text(
                                end.format(
                                    DateTimeFormatter.ofPattern("dd").withLocale(locale) // Day
                                ),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier
                                    .offset(y = (-6).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}