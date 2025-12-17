package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.AuthTextButton
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.images.AppLogo
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.screens.components.textfields.PasswordField
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.extraLargePadding
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun LoginScreen(onClick: () -> Unit) {
    val view = LocalView.current
    val authVM: AuthViewModel = viewModel()
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPassword by remember { mutableStateOf(false) }

    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()

    if (showForgotPassword) {
        ForgotPasswordScreen {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            showForgotPassword = false
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = regularPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.twenty_four_hours_kulturprogramm),
                maxLines = 2,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(regularPadding))

            AppLogo()

            Spacer(Modifier.height(regularPadding))

            EmailField(
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.enter_your_email),
                email = email,
                onEmailChanged = { email = it }
            )

            Spacer(Modifier.height(mediumPadding))

            PasswordField(
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.enter_your_password),
                password = password,
                onPasswordChanged = { password = it }
            )

            if (errorMessageResId != null) {
                ErrorMessage(errorMessageResId!!)
            }

            LargeDarkButton(
                label = stringResource(R.string.login),
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    authVM.login(email, password)
                }
            )

            Spacer(Modifier.height(extraLargePadding))

            AuthTextButton(
                label = stringResource(R.string.forgot_password),
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    showForgotPassword = true
                }
            )

            Spacer(Modifier.weight(1f))

            SignupPrompt(onClick)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            authVM.clearErrorMessages()
        }
    }
}

@Composable
private fun ErrorMessage(errorMessageResId: Int) {
    Text(
        stringResource(errorMessageResId),
        Modifier.padding(top = errorPadding),
        color = Color.Red,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun SignupPrompt(onClick: () -> Unit) {
    Text(
        stringResource(R.string.dont_have_an_account),
        color = Color.Gray,
        textAlign = TextAlign.Center
    )

    AuthTextButton(
        stringResource(R.string.create_account),
        onClick = onClick
    )
}