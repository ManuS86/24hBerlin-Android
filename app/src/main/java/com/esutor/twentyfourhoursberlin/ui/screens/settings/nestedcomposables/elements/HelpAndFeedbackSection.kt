package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.SettingsButton
import com.esutor.twentyfourhoursberlin.ui.theme.microPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel

@Composable
fun HelpAndFeedbackSection(
    context: Context,
    settingsVM: SettingsViewModel = viewModel()
) {
// Report a Problem
    SettingsButton(
        label = stringResource(R.string.report_a_problem),
        fontWeight = Normal,
        textAlign = Start,
        onClick = { settingsVM.openBugReport() }
    )

    Spacer(Modifier.padding(microPadding))

    // Privacy Policy
    SettingsButton(
        label = stringResource(R.string.privacy_policy),
        fontWeight = Normal,
        textAlign = Start,
        onClick = {
            val webUri = "https://www.twenty-four-hours.info/datenschutz/".toUri()
            context.startActivity(Intent(ACTION_VIEW, webUri))
        }
    )

    Spacer(Modifier.padding(microPadding))

    // Terms of Service
    SettingsButton(
        label = stringResource(R.string.terms_of_service),
        fontWeight = Normal,
        textAlign = Start,
        onClick = {
            val webUri = "https://www.twenty-four-hours.info/nutzungsbedingungen/".toUri()
            context.startActivity(Intent(ACTION_VIEW, webUri))
        }
    )
}