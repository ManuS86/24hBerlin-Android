package com.example.a24hberlin.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.settingsFieldStyle(): Modifier = this
    .background(Color.White)
    .clip(RoundedCornerShape(slightRounding))