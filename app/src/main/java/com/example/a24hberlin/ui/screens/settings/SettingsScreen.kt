package com.example.a24hberlin.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.a24hberlin.R
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.settingsFieldStyle
import com.example.a24hberlin.utils.slightRounding
import com.example.a24hberlin.utils.smallPadding

@Composable
fun SettingsScreen() {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(regularPadding)
        ) {
            Text(
                "Account Details",
                fontWeight = FontWeight.Medium
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .settingsFieldStyle()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(mediumPadding)
                        .clip(RoundedCornerShape(slightRounding)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "change_email",
                        Modifier.padding(mediumPadding)
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        tint = Color.Gray,
                        contentDescription = "Change Email",
                        modifier = Modifier.padding(end = mediumPadding)
                    )
                }

                HorizontalDivider()

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(mediumPadding)
                        .clip(RoundedCornerShape(slightRounding)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "change_password",
                        Modifier.padding(mediumPadding)
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        tint = Color.Gray,
                        contentDescription = "Change Password",
                        modifier = Modifier.padding(end = mediumPadding)
                    )
                }
            }
        }
    }
}