package com.example.a24hberlin.ui.screens.settings

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.a24hberlin.R
import com.example.a24hberlin.managers.LanguageChangeHelper
import com.example.a24hberlin.ui.screens.components.buttons.SettingsButton
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.ui.theme.regularPadding
import com.example.a24hberlin.ui.theme.smallPadding
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements.AccountDetailsSection
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements.AccountManagementSection
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements.AppSettingsSection
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements.HelpAndFeedbackSection
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements.SettingsOverlays

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val eventVM: EventViewModel = viewModel(viewModelStoreOwner = context as ComponentActivity)
    val settingsVM: SettingsViewModel = viewModel()

    val scrollState = rememberScrollState()

    val isBugReportSheetOpen by settingsVM.isBugReportSheetOpen.collectAsStateWithLifecycle()
    val bugReportAlertMessage by settingsVM.bugReportAlertMessage.collectAsStateWithLifecycle()
    val showLogoutAlert by settingsVM.showLogoutAlert.collectAsStateWithLifecycle()
    val showDeleteAccountAlert by settingsVM.showDeleteAccountAlert.collectAsStateWithLifecycle()
    val bookmarks by eventVM.bookmarks.collectAsStateWithLifecycle()
    val language by settingsVM.language.collectAsStateWithLifecycle()
    val pushNotificationsEnabled by settingsVM.pushNotificationsEnabledState.collectAsStateWithLifecycle()

    val languageChangeHelper = remember { LanguageChangeHelper() }
    var previousLanguageCode by remember {
        mutableStateOf(language?.languageCode ?: Resources.getSystem().configuration.locales.get(0).language)
    }

    LaunchedEffect(language) {
        val languageCode = language?.languageCode
            ?: Resources.getSystem().configuration.locales.get(1).language

        if (languageCode != previousLanguageCode) {
            previousLanguageCode = languageCode
            languageChangeHelper.setLanguage(context, languageCode)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = regularPadding)
            .padding(bottom = regularPadding)
    ) {
        // A. Account Details
        SettingsSectionTitle(R.string.account_details)
        AccountDetailsSection(navController, bottomBarState, haptic)

        // B. App Settings
        SettingsSectionTitle(R.string.app_settings)
        AppSettingsSection(language, settingsVM, pushNotificationsEnabled, bookmarks, eventVM, haptic)

        // C. Help and Feedback
        SettingsSectionTitle(R.string.help_and_feedback)
        HelpAndFeedbackSection(context, settingsVM)

        Spacer(Modifier.padding(regularPadding))

        // D. Share App
        SettingsButton(
            label = stringResource(R.string.share_24hBerlin),
            fontWeight = Normal,
            textAlign = Start,
            onClick = {
                val intent = Intent(ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=com.example.a24hberlin"
                    )
                }
                context.startActivity(Intent.createChooser(intent, "Share Link"))
            }
        )

        Spacer(Modifier.padding(regularPadding))

        // E. Session Management and Version
        AccountManagementSection(
            onLogoutClick = { settingsVM.toggleLogoutAlert(true) },
            onDeleteClick = { settingsVM.toggleDeleteAlert(true) }
        )
    }

    // F. Alerts and BottomSheets
    SettingsOverlays(
        context = context,
        settingsVM = settingsVM,
        isBugReportSheetOpen = isBugReportSheetOpen,
        bugReportAlertMessage = bugReportAlertMessage,
        showLogoutAlert = showLogoutAlert,
        showDeleteAccountAlert = showDeleteAccountAlert
    )
}

@Composable
private fun SettingsSectionTitle(titleResId: Int) {
    Text(
        text = stringResource(titleResId),
        modifier = Modifier
            .padding(top = regularPadding)
            .padding(bottom = smallPadding),
        fontWeight = Medium,
        color = Black
    )
}