package com.esutor.twentyfourhoursberlin.ui.screens.auth.nestedcomposables

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.AuthTextButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeDarkButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthTextField
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.TitleHeader
import com.esutor.twentyfourhoursberlin.ui.viewmodel.AuthViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.errorPadding
import com.esutor.twentyfourhoursberlin.ui.theme.doublePadding
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Suppress("AssignedValueIsNeverRead")
@Composable
fun LoginScreen(
    authVM: AuthViewModel,
    onClick: () -> Unit
) {
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showForgotPassword by rememberSaveable { mutableStateOf(false) }

    if (showForgotPassword) {
        ForgotPasswordScreen(authVM) { showForgotPassword = false }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = regularPadding)
                .padding(top = halfPadding),
            horizontalAlignment = CenterHorizontally
        ) {
            TitleHeader(Modifier.height(regularPadding))

            AuthTextField(
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.enter_your_email),
                value = email,
                onValueChange = { email = it },
                isPasswordField = false
            )

            Spacer(Modifier.height(halfPadding))

            AuthTextField(
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.enter_your_password),
                value = password,
                onValueChange = { password = it },
                isPasswordField = true
            )

            if (errorMessageResId != null) {
                ErrorMessage(errorMessageResId!!)
            }

            LargeDarkButton(stringResource(R.string.login)) { authVM.login(email, password) }
            Spacer(Modifier.height(doublePadding))
            AuthTextButton(stringResource(R.string.forgot_password)) { showForgotPassword = true }
            Spacer(Modifier.weight(1f))
            SignupPrompt(onClick)
            Spacer(Modifier.height(halfPadding))
        }
    }

    DisposableEffect(Unit) {
        onDispose { authVM.clearErrorMessages() }
    }
}

@Composable
private fun ErrorMessage(errorMessageResId: Int) {
    Text(
        text = stringResource(errorMessageResId),
        modifier = Modifier.padding(top = errorPadding),
        color = Red,
        style = typography.bodyMedium
    )
}

@Composable
private fun SignupPrompt(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.dont_have_an_account),
        color = Gray,
        textAlign = Center
    )

    AuthTextButton(stringResource(R.string.create_account),onClick)
}