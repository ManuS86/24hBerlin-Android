package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.AuthTextButton
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.images.AppLogo
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.screens.components.textfields.PasswordField
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.ui.theme.errorPadding
import com.example.a24hberlin.ui.theme.doublePadding
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Suppress("AssignedValueIsNeverRead")
@Composable
fun LoginScreen(onClick: () -> Unit) {
    val authVM: AuthViewModel = viewModel()
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPassword by remember { mutableStateOf(false) }

    if (showForgotPassword) {
        ForgotPasswordScreen { showForgotPassword = false }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = regularPadding),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(Modifier.height(halfPadding))

            Text(
                text = stringResource(R.string.twenty_four_hours_kulturprogramm),
                maxLines = 2,
                fontWeight = Black,
                textAlign = Center,
                style = typography.headlineLarge
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

            Spacer(Modifier.height(halfPadding))

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
                onClick = { authVM.login(email, password) }
            )

            Spacer(Modifier.height(doublePadding))

            AuthTextButton(
                label = stringResource(R.string.forgot_password),
                onClick = { showForgotPassword = true }
            )

            Spacer(Modifier.weight(1f))

            SignupPrompt(onClick)

            Spacer(Modifier.height(halfPadding))
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
        color = Red,
        style = typography.bodyMedium
    )
}

@Composable
private fun SignupPrompt(onClick: () -> Unit) {
    Text(
        stringResource(R.string.dont_have_an_account),
        color = Gray,
        textAlign = Center
    )

    AuthTextButton(
        stringResource(R.string.create_account),
        onClick = onClick
    )
}