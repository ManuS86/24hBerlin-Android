package com.esutor.twentyfourhoursberlin.utils

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

fun Modifier.singleClick(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    indication: Indication? = ripple()
): Modifier = composed {
    val debounceTime = 800L
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(
        interactionSource = interactionSource,
        indication =  indication,
        enabled = true,
        role = Role.Button
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

@Composable
fun rememberSingleClick(
    debounceTime: Long = 800L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}