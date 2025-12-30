package com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.theme.logoSize

@Composable
fun TitleHeader(modifier: Modifier) {
    Text(
        text = stringResource(R.string.twenty_four_hours),
        maxLines = 1,
        fontWeight = Black,
        textAlign = Center,
        style = typography.headlineLarge
    )

    Text(
        text = stringResource(R.string.kulturprogramm),
        maxLines = 1,
        fontWeight = Black,
        textAlign = Center,
        style = typography.headlineLarge
    )

    Spacer(modifier)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "App Logo",
            contentScale = FillBounds,
            modifier = Modifier.size(logoSize)
        )
    }

    Spacer(modifier)
}