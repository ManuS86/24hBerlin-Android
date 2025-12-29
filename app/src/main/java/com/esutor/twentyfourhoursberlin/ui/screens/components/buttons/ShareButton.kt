package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.roundRipple

@Composable
fun ShareButton(
    context: Context,
    permalink: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        imageVector = Default.Share,
        contentDescription = stringResource(R.string.share),
        modifier = Modifier
            .expressivePop(interactionSource, PopSpeed.Fast)
            .clickable(
                interactionSource = interactionSource,
                indication = roundRipple,
                role = Role.Button,
                onClick = {
                    val intent = Intent(ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(EXTRA_TEXT, permalink)
                    }
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            context.getString(R.string.share_link)
                        )
                    )
                }
            )
    )
}