package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeBlackButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthTextField
import com.esutor.twentyfourhoursberlin.ui.theme.largePadding
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel

@Composable
fun PasswordReAuthForm(
    settingsVM: SettingsViewModel,
    firebaseError: String?,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = standardPadding),
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
                modifier = Modifier.padding(top = smallPadding),
                color = Red,
                style = typography.bodyMedium
            )
        }

        LargeBlackButton(stringResource(R.string.verify_password)) { settingsVM.reAuthenticate(password) }
        Spacer(Modifier.weight(1f))
    }
}