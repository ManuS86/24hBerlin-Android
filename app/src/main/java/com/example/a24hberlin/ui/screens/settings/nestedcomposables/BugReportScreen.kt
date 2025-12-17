package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.theme.TextOffBlack
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.slightRounding

@Composable
fun BugReportScreen(
    onSend: (String) -> Unit
) {
    var bugReport by remember { mutableStateOf("") }

    Column(Modifier.padding(horizontal = regularPadding)) {
        Text(
            text = stringResource(R.string.report_a_bug),
            modifier = Modifier.padding(bottom = mediumPadding),
            style = MaterialTheme.typography.titleMedium,
            color = TextOffBlack
        )

        OutlinedTextField(
            value = bugReport,
            onValueChange = { bugReport = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = Int.MAX_VALUE,
            shape = RoundedCornerShape(slightRounding),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray.copy(0.5f),
                unfocusedBorderColor = Color.Gray.copy(0.5f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        LargeDarkButton(
            label = stringResource(R.string.send_bug_report),
            onClick = {
                onSend(bugReport)
                if (bugReport.isNotBlank()) {
                    bugReport = ""
                }
            }
        )

        Spacer(Modifier.padding(largePadding))
    }
}