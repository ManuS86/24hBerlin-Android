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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
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
                contentDescription = "Event Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(eventImageSize)
                    .clip(RoundedCornerShape(mediumRounding))
                    .padding(end = smallPadding)
            )
        }

        CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(fontWeight = FontWeight.Black)) {
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
                            DateTimeFormatter.ofPattern("EE").withLocale(locale) // Weekday
                        ).uppercase(),
                        fontSize = 11.sp,
                        modifier = Modifier.offset(y = 3.dp)
                    )
                    Text(
                        start.format(
                            DateTimeFormatter.ofPattern("dd").withLocale(locale) // Day
                        ),
                        fontSize = 24.sp
                    )
                    Text(
                        start.format(
                            DateTimeFormatter.ofPattern("MMM").withLocale(locale) // Month
                        ).uppercase(),
                        fontSize = 11.sp,
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
                                fontSize = 17.sp,
                                modifier = Modifier.padding(top = mediumPadding)
                            )

                            Column {
                                Text(
                                    end.format(
                                        DateTimeFormatter.ofPattern("EE")
                                            .withLocale(locale) // Weekday
                                    ).uppercase(),
                                    fontSize = 9.sp,
                                    modifier = Modifier
                                        .offset(y = 2.dp)
                                        .padding(top = 2.dp)
                                )
                                Text(
                                    end.format(
                                        DateTimeFormatter.ofPattern("dd").withLocale(locale) // Day
                                    ),
                                    fontSize = 17.sp,
                                    modifier = Modifier
                                        .offset(y = -mediumPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}