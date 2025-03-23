package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

import android.content.Intent
import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.Party
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun LearnmoreLinkCard(link: String?) {
    val context = LocalContext.current
    val view = LocalView.current

    link?.let {
        Card(
            Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    role = Role.Button,
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)

                        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                        context.startActivity(intent)
                    }
                ),
            shape = RoundedCornerShape(mediumRounding),
            colors = CardDefaults.cardColors(
                containerColor = Details
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(regularPadding)
            ) {
                Icon(
                    Icons.Default.Link,
                    contentDescription = null,
                    Modifier.padding(end = mediumPadding),
                    tint = Party
                )

                Text(
                    stringResource(R.string.further_information),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}