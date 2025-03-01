package com.example.a24hberlin.ui.screens.settings

import android.content.Intent
import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.ui.screens.components.buttons.SettingsButton
import com.example.a24hberlin.ui.screens.components.utilityelements.LanguageDropdown
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.BugReportScreen
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.YesNoAlert
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.utils.LanguageChangeHelper
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.logoSizeSmall
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.slightRounding
import com.example.a24hberlin.utils.smallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val eventVM: EventViewModel = viewModel()
    val settingsVM: SettingsViewModel = viewModel()
    val languageChangeHelper by lazy {
        LanguageChangeHelper()
    }
    val navController = rememberNavController()
    var alertMessage by remember { mutableStateOf("") }
    val pleaseDescribeBug = rememberUpdatedState(stringResource(R.string.please_describe_the_bug))
    val reportThankYou = rememberUpdatedState(stringResource(R.string.thank_you_for_your_report))
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBugReportAlert by remember { mutableStateOf(false) }
    var showDeleteAccountAlert by remember { mutableStateOf(false) }
    var showLogOutAlert by remember { mutableStateOf(false) }
    var showBugReport by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(regularPadding)
    ) {
        Text(
            "Account Details",
            Modifier.padding(bottom = smallPadding),
            fontWeight = FontWeight.Medium
        )

        Card(
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(slightRounding),
                    ambientColor = Color.Gray.copy(0.5f),
                    spotColor = Color.Gray.copy(0.5f)
                ),
            shape = RoundedCornerShape(slightRounding),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(regularPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.change_email))

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    tint = Color.Gray,
                    contentDescription = stringResource(R.string.change_email),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
//                                navController.navigate(Screen.ReAuthWrapper.route)
                        }
                )
            }

            HorizontalDivider(
                Modifier
                    .padding(horizontal = regularPadding),
                color = Color.LightGray
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(regularPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.change_password))

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    tint = Color.Gray,
                    contentDescription = stringResource(R.string.change_password),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
//                                navController.navigate(Screen.ReAuthWrapper.route)
                        }
                )
            }
        }

        Text(
            stringResource(R.string.app_settings),
            Modifier
                .padding(top = regularPadding)
                .padding(bottom = smallPadding),
            fontWeight = FontWeight.Medium
        )

        Card(
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(slightRounding),
                    ambientColor = Color.Gray.copy(0.5f),
                    spotColor = Color.Gray.copy(0.5f)
                ),
            shape = RoundedCornerShape(slightRounding),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = regularPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.language_settings),
                    Modifier.padding(vertical = regularPadding)
                )

                Spacer(Modifier.weight(1f))

                LanguageDropdown(
                    label = stringResource(R.string.system_default),
                    selectedValue = settingsVM.language,
                    onValueSelected = {
                        settingsVM.updateLanguage(it)
                        languageChangeHelper.setLanguage(
                            context,
                            settingsVM.language?.languageCode
                                ?: Resources.getSystem().configuration.locales.get(1).language
                        )
                    },
                    options = Language.allValues.toList()
                )
            }
        }

        Spacer(Modifier.padding(mediumPadding))

        Card(
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(slightRounding),
                    ambientColor = Color.Gray.copy(0.5f),
                    spotColor = Color.Gray.copy(0.5f)
                ),
            shape = RoundedCornerShape(slightRounding),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = regularPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.push_notifications),
                    Modifier.padding(vertical = regularPadding)
                )

                Spacer(Modifier.weight(1f))

                Switch(
                    checked = settingsVM.pushNotificationsEnabled,
                    onCheckedChange = {
                        settingsVM.changePushNotifications(it)
                        if (it) {
                            eventVM.favorites?.forEach { favorite ->
                                eventVM.addFavoritePushNotification(favorite, 3, 11)
                                eventVM.addFavoritePushNotification(favorite, 0, 11)
                                eventVM.addFavoritePushNotification(favorite, 0, 2)
                                eventVM.setupAbsenceReminder()
                            }
                        } else {
                            settingsVM.removeAllPendingNotifications()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = Color.LightGray.copy(0.5f)
                    )
                )
            }
        }

        Text(
            "Community",
            Modifier
                .padding(top = regularPadding)
                .padding(bottom = smallPadding),
            fontWeight = FontWeight.Medium
        )

        SettingsButton(
            stringResource(R.string.share_24hBerlin),
            FontWeight.Normal,
            TextAlign.Start
        ) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=com.example.a24hberlin"
                )
            }

            context.startActivity(
                Intent.createChooser(
                    intent,
                    "Share Link"
                )
            )
        }

        Spacer(Modifier.padding(mediumPadding))

        SettingsButton(
            stringResource(R.string.report_a_bug),
            FontWeight.Normal,
            TextAlign.Start
        ) {
            showBugReport = !showBugReport
        }

        Spacer(Modifier.padding(largePadding))

        SettingsButton(
            stringResource(R.string.logout),
            FontWeight.SemiBold,
            TextAlign.Center
        ) {
            showLogOutAlert = true
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(40.dp),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                Modifier
                    .size(logoSizeSmall)
            )

            Text(
                "Version 1.0",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        SettingsButton(
            stringResource(R.string.delete_account),
            FontWeight.Normal,
            TextAlign.Center
        ) {
            showDeleteAccountAlert = true
        }

        if (showBugReport) {
            ModalBottomSheet(
                onDismissRequest = { showBugReport = false },
                containerColor = Color.White,
                sheetState = sheetState
            ) {
                BugReportScreen(
                    { showBugReportAlert = !showBugReportAlert },
                    { alertMessage = pleaseDescribeBug.value },
                    { alertMessage = reportThankYou.value }
                )
            }
        }

        if (showLogOutAlert) {
            YesNoAlert(
                stringResource(R.string.logout),
                stringResource(R.string.are_you_sure_you_want_to_log_out_q),
                { showLogOutAlert = false },
                {
                    showLogOutAlert = false
                    settingsVM.logout()
                }
            )
        }

        if (showDeleteAccountAlert) {
            YesNoAlert(
                stringResource(R.string.delete_account),
                stringResource(R.string.are_you_sure_you_want_to_delete_your_account),
                { showDeleteAccountAlert = false },
                {
                    showDeleteAccountAlert = false
                    settingsVM.deleteAccount()
                }
            )
        }

        if (showBugReportAlert) {
            AlertDialog(
                onDismissRequest = { showBugReportAlert = false },
                title = { Text(stringResource(R.string.bug_report)) },
                text = { Text(alertMessage) },
                containerColor = Color.White,
                confirmButton = {
                    TextButton(
                        onClick = {
                            showBugReportAlert = false
                            if (alertMessage == reportThankYou.value) {
                                showBugReport = false
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}