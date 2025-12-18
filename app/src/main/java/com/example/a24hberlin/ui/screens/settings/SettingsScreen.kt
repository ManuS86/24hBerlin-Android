package com.example.a24hberlin.ui.screens.settings

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_TEXT
import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.managers.LanguageChangeHelper
import com.example.a24hberlin.navigation.Screen
import com.example.a24hberlin.ui.screens.components.buttons.SettingsButton
import com.example.a24hberlin.ui.screens.components.utilityelements.LanguageDropdown
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.BugReportScreen
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.YesNoAlert
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.ui.theme.largePadding
import com.example.a24hberlin.ui.theme.logoSizeSmall
import com.example.a24hberlin.ui.theme.mediumPadding
import com.example.a24hberlin.ui.theme.regularPadding
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.theme.smallPadding
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val eventVM: EventViewModel = viewModel()
    val settingsVM: SettingsViewModel = viewModel()

    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isBugReportSheetOpen by settingsVM.isBugReportSheetOpen.collectAsStateWithLifecycle()
    val bugReportAlertMessage by settingsVM.bugReportAlertMessage.collectAsStateWithLifecycle()
    val showLogoutAlert by settingsVM.showLogoutAlert.collectAsStateWithLifecycle()
    val showDeleteAccountAlert by settingsVM.showDeleteAccountAlert.collectAsStateWithLifecycle()
    val bookmarks by eventVM.bookmarks.collectAsStateWithLifecycle()
    val language by settingsVM.language.collectAsStateWithLifecycle()
    val pushNotificationsEnabled by settingsVM.pushNotificationsEnabledState.collectAsStateWithLifecycle()

    val languageChangeHelper = remember { LanguageChangeHelper() }
    var previousLanguageCode by remember { mutableStateOf("") }

    val pleaseDescribeBug = stringResource(R.string.please_describe_the_bug)
    val reportThankYou = stringResource(R.string.thank_you_for_your_report)

    LaunchedEffect(Unit) {
        previousLanguageCode = language?.languageCode
            ?: Resources.getSystem().configuration.locales.get(1).language
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
            .padding(regularPadding)
    ) {
        // A. Account Details
        SettingsSectionTitle(R.string.account_details)
        AccountDetailsCard(navController, bottomBarState, haptic)

        // B. App Settings
        SettingsSectionTitle(R.string.app_settings)
        AppSettingsCard(language, settingsVM, pushNotificationsEnabled, bookmarks, eventVM, haptic)

        // C. Community & Utility
        SettingsSectionTitle(R.string.help_and_feedback)

        // Report a Bug
        SettingsButton(
            label = stringResource(R.string.report_a_bug),
            fontWeight = Normal,
            textAlign = Start,
            onClick = { settingsVM.openBugReport() }
        )

        Spacer(Modifier.padding(mediumPadding))

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

        Spacer(Modifier.padding(mediumPadding))

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

        Spacer(Modifier.padding(largePadding))

        // Share App
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

        Spacer(Modifier.padding(largePadding))

        // Log Out
        SettingsButton(
            label = stringResource(R.string.logout),
            fontWeight = SemiBold,
            textAlign = Center,
            onClick = { settingsVM.toggleLogoutAlert(true) }
        )

        // D. Footer and Version
        AppFooter()

        // Delete Account
        SettingsButton(
            label = stringResource(R.string.delete_account),
            fontWeight = Normal,
            textAlign = Center,
            onClick = { settingsVM.toggleDeleteAlert(true) }
        )
    }

    // Bug Report Modal Sheet
    if (isBugReportSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { settingsVM.closeBugReport() },
            containerColor = White,
            sheetState = sheetState
        ) {
            BugReportScreen(
                onSend = { report ->
                    settingsVM.sendBugReport(
                        report,
                        pleaseDescribeBug,
                        reportThankYou
                    )
                }
            )
        }
    }

    // Bug Report Status Alert (OK only)
    bugReportAlertMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                settingsVM.setBugReportAlert(null)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsVM.setBugReportAlert(null)
                        if (message == reportThankYou) settingsVM.closeBugReport()
                    }
                ) { Text("OK") }
            },
            title = { Text(stringResource(R.string.bug_report)) },
            text = { Text(message) },
            containerColor = White
        )
    }

    // Log Out Alert
    if (showLogoutAlert) {
        YesNoAlert(
            title = stringResource(R.string.logout),
            body = stringResource(R.string.are_you_sure_you_want_to_log_out_q),
            onNo = {
                settingsVM.toggleLogoutAlert(false)
            },
            onYes = {
                settingsVM.toggleLogoutAlert(false)
                settingsVM.logout()
            }
        )
    }

    // Delete Account Alert
    if (showDeleteAccountAlert) {
        YesNoAlert(
            title = stringResource(R.string.delete_account),
            body = stringResource(R.string.are_you_sure_you_want_to_delete_your_account),
            onNo = {
                settingsVM.toggleDeleteAlert(false)
            },
            onYes = {
                settingsVM.toggleDeleteAlert(false)
                settingsVM.deleteAccount()
            }
        )
    }
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

@Composable
private fun AccountDetailsCard(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    haptic: HapticFeedback
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(slightRounding),
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = RoundedCornerShape(slightRounding),
        colors = cardColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        // Change Email
        SettingsCardItem(
            title = stringResource(R.string.change_email),
            haptic = haptic,
            onClick = {
                bottomBarState.value = false
                navController.navigate(Screen.ReAuthWrapper.createRoute("email"))
            }
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = regularPadding),
            color = LightGray
        )

        // Change Password
        SettingsCardItem(
            title = stringResource(R.string.change_password),
            haptic = haptic,
            onClick = {
                bottomBarState.value = false
                navController.navigate(Screen.ReAuthWrapper.createRoute("password"))
            }
        )
    }
}

@Composable
private fun SettingsCardItem(
    title: String,
    haptic: HapticFeedback,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    onClick()
                }
            )
            .padding(regularPadding),
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

@Composable
private fun AppSettingsCard(
    language: Language?,
    settingsVM: SettingsViewModel,
    pushNotificationsEnabled: Boolean,
    bookmarks: List<Event>,
    eventVM: EventViewModel,
    haptic: HapticFeedback
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(slightRounding),
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = RoundedCornerShape(slightRounding),
        colors = cardColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        // Language Settings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = regularPadding),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.language_settings),
                modifier = Modifier.padding(vertical = regularPadding)
            )

            Spacer(Modifier.weight(1f))

            LanguageDropdown(
                label = stringResource(R.string.system_default),
                selectedValue = language,
                onValueSelected = { settingsVM.changeLanguage(it) },
                options = Language.allValues.toList()
            )
        }
    }

    Spacer(Modifier.padding(mediumPadding))

    // Push Notifications
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(slightRounding),
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = RoundedCornerShape(slightRounding),
        colors = cardColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = regularPadding),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.push_notifications),
                modifier = Modifier.padding(vertical = regularPadding)
            )

            Spacer(Modifier.weight(1f))

            Switch(
                checked = pushNotificationsEnabled,
                onCheckedChange = { isChecked ->
                    haptic.performHapticFeedback(TextHandleMove)
                    settingsVM.changePushNotifications(isChecked)
                    if (isChecked) {
                        bookmarks.forEach { bookmark ->
                            eventVM.addBookmarkPushNotifications(bookmark)
                        }
                        eventVM.setupAbsenceReminder()
                    } else {
                        settingsVM.removeAllPendingNotifications(bookmarks)
                    }
                },
                colors = SwitchDefaults.colors(uncheckedTrackColor = LightGray.copy(0.5f))
            )
        }
    }
}

@Composable
private fun AppFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        verticalArrangement = spacedBy(mediumPadding),
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