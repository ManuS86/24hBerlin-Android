package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeBlackButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthTextField
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthMessages
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.largePadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding

@Composable
fun ChangeEmailScreen(
    settingsVM: SettingsViewModel
) {
    val confirmationMessageResId by settingsVM.confirmationMessageResId.collectAsStateWithLifecycle()
    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }

    DisposableEffect(Unit) { onDispose { settingsVM.clearErrorMessages() } }

    Box(Modifier.fillMaxSize()) {
        Background()

        Column(
            modifier = Modifier.padding(horizontal = standardPadding),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.7f))

            Text(
                text = stringResource(R.string.change_your_email),
                modifier = Modifier.padding(vertical = largePadding),
                fontWeight = Bold,
                style = typography.titleLarge,
                color = Black
            )

            AuthTextField(
                label = stringResource(R.string.new_email),
                placeholder = stringResource(R.string.enter_your_new_email),
                value = email,
                onValueChange = { email = it },
                isPasswordField = false
            )

            AuthMessages(
                confirmationMessageResId = confirmationMessageResId,
                firebaseError = firebaseError
            )

            LargeBlackButton(stringResource(R.string.change_email)) { settingsVM.changeEmail(email) }
            Spacer(Modifier.weight(1f))
        }
    }
}