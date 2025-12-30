package com.esutor.twentyfourhoursberlin.ui.screens.settings

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.SettingsButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.ui.theme.extraSmallPadding
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements.AccountDetailsSection
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements.AccountManagementSection
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements.AppSettingsSection
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements.HelpAndFeedbackSection
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements.SettingsOverlays

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    settingsVM: SettingsViewModel,
    eventVM: EventViewModel
) {
    // --- Context & Helpers ---
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // --- State observation ---
    val language by settingsVM.language.collectAsStateWithLifecycle()
    val isBugReportSheetOpen by settingsVM.isProblemReportSheetOpen.collectAsStateWithLifecycle()
    val bugReportAlertMessage by settingsVM.problemReportAlertMessage.collectAsStateWithLifecycle()
    val showLogoutAlert by settingsVM.showLogoutAlert.collectAsStateWithLifecycle()
    val showDeleteAccountAlert by settingsVM.showDeleteAccountAlert.collectAsStateWithLifecycle()
    val notificationsEnabled by settingsVM.notificationsEnabledState.collectAsStateWithLifecycle()
    val bookmarks by eventVM.bookmarks.collectAsStateWithLifecycle()

    // --- Actions ---
    val onShareApp = {
        val intent = Intent(ACTION_SEND).apply {
            type = "text/plain"
            putExtra(EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.esutor.twentyfourhoursberlin")
        }
        context.startActivity(Intent.createChooser(intent, "Share Link"))
    }

    // --- UI layout ---
    Box(Modifier.fillMaxSize()) {
        Background()

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = standardPadding)
                .padding(bottom = standardPadding)
        ) {
            SettingsSectionTitle(R.string.account_details)
            AccountDetailsSection(navController)

            SettingsSectionTitle(R.string.app_settings)
            AppSettingsSection(
                language = language,
                notificationsEnabled = notificationsEnabled,
                bookmarks = bookmarks ?: emptyList(),
                settingsVM = settingsVM,
                eventVM = eventVM
            )

            SettingsSectionTitle(R.string.help_and_feedback)
            HelpAndFeedbackSection(context, settingsVM)

            Spacer(Modifier.padding(standardPadding))

            SettingsButton(
                label = stringResource(R.string.share_24hBerlin),
                fontWeight = Normal,
                textAlign = Start,
                onClick = onShareApp
            )

            Spacer(Modifier.padding(standardPadding))

            AccountManagementSection(
                onLogoutClick = { settingsVM.toggleLogoutAlert(true) },
                onDeleteClick = { settingsVM.toggleDeleteAlert(true) }
            )
        }

        SettingsOverlays(
            context = context,
            settingsVM = settingsVM,
            isBugReportSheetOpen = isBugReportSheetOpen,
            bugReportAlertMessage = bugReportAlertMessage,
            showLogoutAlert = showLogoutAlert,
            showDeleteAccountAlert = showDeleteAccountAlert
        )
    }
}

@Composable
private fun SettingsSectionTitle(titleResId: Int) {
    Text(
        text = stringResource(titleResId),
        modifier = Modifier
            .padding(top = standardPadding)
            .padding(bottom = extraSmallPadding),
        fontWeight = Medium,
        color = Black
    )
}