package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.navigation.Screen
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding
import com.esutor.twentyfourhoursberlin.utils.singleClick

@Composable
fun AccountDetailsSection(
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = slightRounding,
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = slightRounding,
        colors = cardColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        // Change Email
        SettingsCardItem(
            title = stringResource(R.string.change_email),
            onClick = { navController.navigate(Screen.ReAuthWrapper.createRoute("email")) }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = standardPadding), color = LightGray)

        // Change Password
        SettingsCardItem(
            title = stringResource(R.string.change_password),
            onClick = { navController.navigate(Screen.ReAuthWrapper.createRoute("password")) }
        )
    }
}

@Composable
private fun SettingsCardItem(
    title: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .expressivePop(interactionSource)
            .clip(slightRounding)
            .singleClick(interactionSource, onClick)
            .padding(standardPadding),
        verticalAlignment = CenterVertically
    ) {
        Text(title)

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
            tint = Gray,
            contentDescription = title,
            modifier = Modifier
                .size(16.dp)
        )
    }
}