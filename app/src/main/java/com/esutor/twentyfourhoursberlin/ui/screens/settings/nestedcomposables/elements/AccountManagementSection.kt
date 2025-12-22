package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.SettingsButton
import com.esutor.twentyfourhoursberlin.ui.theme.logoSizeSmall
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding

@Composable
fun AccountManagementSection(
    onLogoutClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Log Out
    SettingsButton(
        label = stringResource(R.string.logout),
        fontWeight = SemiBold,
        textAlign = Center,
        onClick = onLogoutClick
    )

    AppFooter()

    // Delete Account
    SettingsButton(
        label = stringResource(R.string.delete_account),
        fontWeight = Normal,
        textAlign = Center,
        onClick = onDeleteClick
    )
}

@Composable
private fun AppFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        verticalArrangement = spacedBy(halfPadding),
        horizontalAlignment = CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(logoSizeSmall)
        )

        Text(
            text = "Version 1.0",
            style = typography.bodySmall,
            color = Gray
        )
    }
}