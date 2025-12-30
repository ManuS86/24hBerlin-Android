package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeDarkButton
import com.esutor.twentyfourhoursberlin.ui.theme.TextOffBlack
import com.esutor.twentyfourhoursberlin.ui.theme.largePadding
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.ui.theme.reportProblemTextFieldHeight
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding
import kotlin.Int.Companion.MAX_VALUE

@Composable
fun ReportProblemScreen(
    onSend: (String) -> Unit
) {
    var bugReport by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(horizontal = standardPadding)) {
        Text(
            text = stringResource(R.string.report_a_problem),
            modifier = Modifier.padding(bottom = smallPadding),
            style = typography.titleMedium,
            color = TextOffBlack
        )

        OutlinedTextField(
            value = bugReport,
            onValueChange = { bugReport = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(reportProblemTextFieldHeight),
            maxLines = MAX_VALUE,
            shape = slightRounding,
            colors = colors(
                focusedBorderColor = Gray.copy(0.5f),
                unfocusedBorderColor = Gray.copy(0.5f),
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )

        LargeDarkButton(stringResource(R.string.send_bug_report)) {
            onSend(bugReport)
            if (bugReport.isNotBlank()) {
                bugReport = ""
            }
        }

        Spacer(Modifier.padding(largePadding))
    }
}