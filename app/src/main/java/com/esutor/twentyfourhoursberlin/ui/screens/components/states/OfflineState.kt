package com.esutor.twentyfourhoursberlin.ui.screens.components.states

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Composable
fun OfflineState() {
    val tintColor = Gray.copy(0.6f)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Background()

        Icon(
            imageVector = Icons.Rounded.WifiOff,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = tintColor
        )

        Spacer(Modifier.height(regularPadding))

        Text(
            text = stringResource(R.string.no_internet_connection),
            fontWeight = SemiBold,
            style = typography.titleMedium,
            color = tintColor
        )
    }
}