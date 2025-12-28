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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeDarkButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthTextField
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.errorPadding
import com.esutor.twentyfourhoursberlin.ui.theme.largePadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Composable
fun ReAuthWrapper(
    from: String,
    settingsVM: SettingsViewModel
) {
    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()
    val isReauthenticated by settingsVM.isReauthenticated.collectAsStateWithLifecycle()

    var password by rememberSaveable { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Background()

        if (isReauthenticated) {
            if (from == "email") ChangeEmailScreen(settingsVM) else ChangePasswordScreen(settingsVM)
        } else {
            PasswordReAuthForm(
                settingsVM = settingsVM,
                firebaseError = firebaseError,
                password = password,
                onPasswordChange = { password = it }
            )
        }
    }

    DisposableEffect(Unit) { onDispose { settingsVM.clearErrorMessages() } }
}

@Composable
private fun PasswordReAuthForm(
    settingsVM: SettingsViewModel,
    firebaseError: String?,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = regularPadding),
        horizontalAlignment = CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.7f))

        Text(
            text = stringResource(R.string.please_re_enter_your_password),
            modifier = Modifier.padding(vertical = largePadding),
            fontWeight = Bold,
            style = typography.titleLarge,
            color = Black
        )

        AuthTextField(
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.re_enter_your_password),
            value = password,
            onValueChange = onPasswordChange,
            isPasswordField = true
        )

        if (firebaseError != null) {
            Text(
                text = firebaseError,
                modifier = Modifier.padding(top = errorPadding),
                color = Red,
                style = typography.bodyMedium
            )
        }

        LargeDarkButton(stringResource(R.string.verify_password)) { settingsVM.reAuthenticate(password) }
        Spacer(Modifier.weight(1f))
    }
}