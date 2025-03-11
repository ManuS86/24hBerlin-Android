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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.PasswordField
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun ChangePasswordScreen() {
    val settingsVM: SettingsViewModel = viewModel()
    var confirmPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val confirmationMessage by settingsVM.confirmationMessage.collectAsStateWithLifecycle()
    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()
    val passwordError by settingsVM.passwordError.collectAsStateWithLifecycle()

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
            Spacer(Modifier.weight(0.7f))

            Text(
                stringResource(R.string.change_your_password),
                Modifier.padding(vertical = largePadding),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            PasswordField(
                stringResource(R.string.new_password),
                stringResource(R.string.enter_your_new_password),
                password
            ) { password = it }

            Spacer(Modifier.height(regularPadding))

            PasswordField(
                stringResource(R.string.confirm_new_password),
                stringResource(R.string.confirm_your_new_password),
                confirmPassword
            ) { confirmPassword = it }

            if (confirmationMessage != null) {
                Text(
                    stringResource(confirmationMessage!!),
                    Modifier.padding(top = errorPadding),
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (firebaseError != null) {
                Text(
                    firebaseError!!,
                    Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (passwordError != null) {
                Text(
                    stringResource(passwordError!!),
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