package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.circle
import com.esutor.twentyfourhoursberlin.ui.theme.googleIconSize
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GoogleSignInButton(
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = standardPadding)
            .fillMaxWidth()
            .expressivePop(interactionSource),
        enabled = !isLoading,
        shape = circle,
        colors = buttonColors(
            contentColor = colorScheme.onSurface,
            containerColor = White
        ),
        border = BorderStroke(1.dp, colorScheme.outlineVariant),
        interactionSource = interactionSource
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(googleIconSize),
                strokeWidth = 2.dp,
                color = colorScheme.primary
            )
        } else {
            Row(verticalAlignment = CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = null,
                    tint = Unspecified,
                    modifier = Modifier.size(googleIconSize)
                )

                Text(
                    text = stringResource(R.string.sign_in_with_google),
                    modifier = Modifier.padding(smallPadding),
                    fontWeight = SemiBold,
                    style = typography.bodyLarge
                )
            }
        }
    }
}