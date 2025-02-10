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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
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
        imageURL?.let {
            AsyncImage(
                model = imageURL,
                placeholder = rememberVectorPainter(image = Icons.Default.Image),
                error = rememberVectorPainter(image = Icons.Default.BrokenImage),
                contentDescription = "Event Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(eventImageSize)
                    .clip(RoundedCornerShape(mediumRounding))
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
                    ).uppercase(),
                    fontSize = 10.sp,
                    modifier = Modifier.offset(y = 3.dp)
                )
                Text(
                    start.format(
                        DateTimeFormatter.ofPattern("dd").withLocale(locale) // Day
                    ),
                    fontSize = 20.sp
                )
                Text(
                    start.format(
                        DateTimeFormatter.ofPattern("MMM").withLocale(locale) // Month
                    ).uppercase(),
                    fontSize = 10.sp,
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
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = mediumPadding)
                        )

                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                end.format(
                                    DateTimeFormatter.ofPattern("EEE").withLocale(locale) // Weekday
                                ).uppercase(),
                                fontSize = 8.sp,
                                modifier = Modifier
                                    .offset(y = 2.dp)
                                    .padding(top = 2.dp)
                            )
                            Text(
                                end.format(
                                    DateTimeFormatter.ofPattern("dd").withLocale(locale) // Day
                                ),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}