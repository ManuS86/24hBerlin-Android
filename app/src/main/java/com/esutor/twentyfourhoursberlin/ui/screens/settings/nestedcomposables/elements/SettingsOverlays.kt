package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import com.esutor.twentyfourhoursberlin.MainActivity
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.YesNoAlert
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.ReportProblemScreen
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsOverlays(
    context: Context,
    settingsVM: SettingsViewModel,
    isBugReportSheetOpen: Boolean,
    bugReportAlertMessage: String?,
    showLogoutAlert: Boolean,
    showDeleteAccountAlert: Boolean
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pleaseDescribeBug = stringResource(R.string.please_describe_the_problem)
    val reportThankYou = stringResource(R.string.thank_you_for_your_report)

    // 1. Problem Report Sheet
    if (isBugReportSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { settingsVM.closeBugReport() },
            containerColor = White,
            sheetState = sheetState
        ) {
            ReportProblemScreen(onSend = { report ->
                settingsVM.sendProblemReport(report, pleaseDescribeBug, reportThankYou)
            })
        }
    }

    // 2. Problem Report Status Alert
    bugReportAlertMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { settingsVM.setProblemReportAlert(null) },
            confirmButton = {
                TextButton(onClick = {
                    settingsVM.setProblemReportAlert(null)
                    if (message == reportThankYou) settingsVM.closeBugReport()
                }) { Text("OK") }
            },
            title = { Text(stringResource(R.string.problem_report)) },
            text = { Text(message) },
            containerColor = White
        )
    }

    // 3. Log Out / Delete Alerts
    if (showLogoutAlert) {
        YesNoAlert(
            title = stringResource(R.string.logout),
            body = stringResource(R.string.are_you_sure_you_want_to_log_out_q),
            onNo = { settingsVM.toggleLogoutAlert(false) },
            onYes = {
                settingsVM.toggleLogoutAlert(false)
                settingsVM.logout()
                restartApp(context)
            }
        )
    }

    if (showDeleteAccountAlert) {
        YesNoAlert(
            title = stringResource(R.string.delete_account),
            body = stringResource(R.string.are_you_sure_you_want_to_delete_your_account),
            onNo = { settingsVM.toggleDeleteAlert(false) },
            onYes = {
                settingsVM.toggleDeleteAlert(false)
                settingsVM.deleteAccount()
                restartApp(context)
            }
        )
    }
}

private fun restartApp(context: Context) {
    val intent = Intent(context, MainActivity::class.java).apply {
        addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
    }
    context.startActivity(intent)
}