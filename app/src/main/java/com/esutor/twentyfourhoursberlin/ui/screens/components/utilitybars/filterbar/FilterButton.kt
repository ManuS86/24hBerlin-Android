package com.esutor.twentyfourhoursberlin.ui.screens.components.utilitybars.filterbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.enums.Month
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.filterButtonHeight
import com.esutor.twentyfourhoursberlin.ui.theme.mediumPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel

@Composable
fun FilterButton(
    selectedMonth: Month?,
    month: Month?,
    eventVM: EventViewModel
) {
    val context = LocalContext.current
    val isSelected = selectedMonth == month
    val monthInteractionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .height(filterButtonHeight)
            .expressivePop(monthInteractionSource, PopSpeed.Fast)
            .background(
                color = DarkGray.copy(alpha = if (isSelected) 0.9f else 0.4f),
                shape = slightRounding
            )
            .padding(horizontal = mediumPadding)
            .clickable(
                interactionSource = monthInteractionSource,
                onClick = { eventVM.updateMonth(if (isSelected) null else month) }
            ),
        contentAlignment = Center
    ) {
        Text(
            text = month?.getStringResource(context) ?: stringResource(R.string.all),
            fontWeight = SemiBold,
            color = White.copy(alpha = if (isSelected) 1f else 0.5f),
            style = typography.bodyMedium
        )
    }
}