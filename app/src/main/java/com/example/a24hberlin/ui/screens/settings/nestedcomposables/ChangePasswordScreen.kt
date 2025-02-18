package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.PasswordField
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.extraLargePadding
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun ChangePasswordScreen() {
    val settingsVM: SettingsViewModel = viewModel()
    var confirmPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            settingsVM.clearErrorMessages()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.padding(horizontal = regularPadding)) {
            Spacer(Modifier.padding(extraLargePadding))

            Text(
                stringResource(R.string.change_your_password),
                Modifier.padding(top = largePadding),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.weight(0.5f))

            PasswordField(
                stringResource(R.string.password),
                stringResource(R.string.please_enter_your_password),
                password
            ) { password = it }

            Spacer(Modifier.height(regularPadding))

            PasswordField(
                stringResource(R.string.confirm_new_password),
                stringResource(R.string.please_confirm_your_new_password),
                confirmPassword
            ) { confirmPassword = it }

            if (settingsVM.confirmationMessage != null) {
                Text(
                    stringResource(settingsVM.confirmationMessage!!),
                    Modifier.padding(top = errorPadding),
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (settingsVM.firebaseErrorMessage != null) {
                Text(
                    settingsVM.firebaseErrorMessage!!,
                    Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (settingsVM.passwordError != null) {
                Text(
                    stringResource(settingsVM.passwordError!!),
                    Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LargeDarkButton(stringResource(R.string.change_password)) {
                settingsVM.changePassword(password, confirmPassword)
            }

            Spacer(Modifier.weight(1f))
        }
    }
}