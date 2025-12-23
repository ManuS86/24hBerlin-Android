package com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.theme.extraLargePadding
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding

@Composable
fun LoadingOverlay(
    progressValue: Float,
    onFinished: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressValue,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "ProgressBarAnimation",
        finishedListener = { if (it >= 1f) onFinished() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {},
        contentAlignment = Center
    ) {
        Background()
        Row(
            modifier = Modifier
                .align(Center)
                .padding(extraLargePadding),
            horizontalArrangement = spacedBy(halfPadding),
            verticalAlignment = CenterVertically
        ) {
            Icon(Icons.Filled.CalendarMonth, null, Modifier.size(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Black.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .background(Black)
                )
            }
        }
    }
}