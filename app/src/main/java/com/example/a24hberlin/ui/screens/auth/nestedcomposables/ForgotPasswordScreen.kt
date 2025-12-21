package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.utilityelements.AuthMessages
import com.example.a24hberlin.ui.screens.components.buttons.AuthTextButton
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.AuthTextField
import com.example.a24hberlin.ui.screens.components.utilityelements.TitleHeader
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.ui.theme.doublePadding
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun ForgotPasswordScreen(onClick: () -> Unit) {
    val authVM: AuthViewModel = viewModel()
    val confirmationMessageResId by authVM.confirmationMessageResId.collectAsStateWithLifecycle()
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()
    val firebaseError by authVM.firebaseError.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = regularPadding)
            .padding(top = halfPadding),
        horizontalAlignment = CenterHorizontally
    ) {
        TitleHeader(Modifier.weight(0.2f))

        AuthTextField(
            label = stringResource(R.string.email),
            placeholder = stringResource(R.string.enter_your_email),
            value = email,
            onValueChange = { email = it },
            isPasswordField = false
        )

        AuthMessages(
            confirmationMessageResId = confirmationMessageResId,
            errorMessageResId = errorMessageResId,
            firebaseError = firebaseError
        )

        LargeDarkButton(
            label = stringResource(R.string.reset_password),
            onClick = { authVM.resetPassword(email) }
        )

        Spacer(Modifier.height(doublePadding))

        AuthTextButton(
            label = stringResource(R.string.try_logging_in_again),
            onClick = onClick
        )

        Spacer(Modifier.weight(1f))
    }

    DisposableEffect(Unit) {
        onDispose {
            authVM.clearErrorMessages()
        }
    }
}