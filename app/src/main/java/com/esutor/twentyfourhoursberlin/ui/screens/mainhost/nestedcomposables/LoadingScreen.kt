package com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
fun LoadingScreen(
    isVisible: Boolean?
) {
    val barExpansion by animateFloatAsState(
        targetValue = if (isVisible ?: false) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "BarExpansion"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = if (isVisible ?: false  && barExpansion > 0.9f) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "ProgressBarAnimation"
    )

    AnimatedVisibility(
        visible = isVisible ?: true,
        enter = fadeIn(),
        exit = fadeOut(tween(durationMillis = 150, easing = LinearEasing))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {},
            contentAlignment = Center
        ) {
            Background()

            Row(
                modifier = Modifier
                    .padding(horizontal = extraLargePadding)
                    .fillMaxWidth(barExpansion),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                // Progress Container
                Box(
                    modifier = Modifier
                        .padding(start = halfPadding)
                        .weight(1f)
                        .height(5.dp)
                        .background(Black.copy(alpha = 0.2f))
                ) {
                    // Actual Progress Bar
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
}