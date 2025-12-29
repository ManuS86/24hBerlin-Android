package com.esutor.twentyfourhoursberlin.ui.screens.components.animations

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

enum class PopSpeed {
    Fast, Default, Slow
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Modifier.expressivePop(
    interactionSource: MutableInteractionSource,
    speed: PopSpeed = PopSpeed.Default,
    useTapBuffer: Boolean = true
): Modifier {
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed = true
                is PressInteraction.Release, is PressInteraction.Cancel -> {
                    if (useTapBuffer) {
                        delay(100)
                    }
                    isPressed = false
                }
            }
        }
    }

    val (targetScale, animationSpec) = when (speed) {
        PopSpeed.Fast -> 0.87f to motionScheme.fastSpatialSpec<Float>()
        PopSpeed.Default -> 0.92f to motionScheme.defaultSpatialSpec()
        PopSpeed.Slow -> 0.97f to motionScheme.slowSpatialSpec()
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) targetScale else 1f,
        animationSpec = animationSpec,
        label = "ExpressivePopScale"
    )

    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}