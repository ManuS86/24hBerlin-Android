package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
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
fun ReAuthWrapper(
    from: String,
    onSetTitleId: (Int?) -> Unit
) {
    val settingsVM: SettingsViewModel = viewModel()

    var password by remember { mutableStateOf("") }

    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()
    val isReauthenticated by settingsVM.isReauthenticated.collectAsStateWithLifecycle()

    val targetTitleId = when {
        isReauthenticated && from == "email" -> R.string.change_email
        isReauthenticated && from == "password" -> R.string.change_password
        else -> R.string.re_authenticate
    }

    LaunchedEffect(targetTitleId) {
        onSetTitleId(targetTitleId)
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        if (isReauthenticated) {
            if (from == "email") {
                ChangeEmailScreen()
            } else {
                ChangePasswordScreen()
            }
        } else {
            PasswordReAuthForm(
                settingsVM = settingsVM,
                firebaseError = firebaseError,
                onPasswordEntered = { password = it },
                password = password
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            settingsVM.clearErrorMessages()
        }
    }
}

@Composable
private fun PasswordReAuthForm(
    settingsVM: SettingsViewModel,
    firebaseError: String?,
    password: String,
    onPasswordEntered: (String) -> Unit
) {
    val view = LocalView.current

    Column(Modifier.padding(horizontal = regularPadding)) {
        Spacer(Modifier.weight(0.7f))

        Text(
            text = stringResource(R.string.please_re_enter_your_password),
            modifier = Modifier.padding(vertical = largePadding),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )

        PasswordField(
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.re_enter_password),
            password = password,
            onPasswordChanged = onPasswordEntered
        )

        if (firebaseError != null) {
            Text(
                text = firebaseError,
                modifier = Modifier.padding(top = errorPadding),
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LargeDarkButton(
            label = stringResource(R.string.verify_password),
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                settingsVM.reAuthenticate(password)
            }
            )

        Spacer(Modifier.weight(1f))
    }
}