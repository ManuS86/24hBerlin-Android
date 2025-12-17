package com.example.a24hberlin.ui.screens.components.buttons

import android.content.Context
import android.content.Intent
import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.a24hberlin.R

@Composable
fun ShareButton(context: Context, permalink: String) {
    val view = LocalView.current

    Icon(
        imageVector = Icons.Default.Share,
        contentDescription = stringResource(R.string.share),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                role = Role.Button,
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, permalink)
                    }
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            R.string.share_link.toString()
                        )
                    )
                }
            )
    )
}