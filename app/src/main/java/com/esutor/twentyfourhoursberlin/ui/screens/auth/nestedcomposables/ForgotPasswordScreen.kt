package com.esutor.twentyfourhoursberlin.ui.screens.auth.nestedcomposables

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
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthMessages
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.AuthTextButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeDarkButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthTextField
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.TitleHeader
import com.esutor.twentyfourhoursberlin.ui.viewmodel.AuthViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.doublePadding
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Composable
fun ForgotPasswordScreen(
    authVM: AuthViewModel,
    onClick: () -> Unit
) {
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

        LargeDarkButton(stringResource(R.string.reset_password)) { authVM.resetPassword(email) }
        Spacer(Modifier.height(doublePadding))
        AuthTextButton(stringResource(R.string.try_logging_in_again), onClick)
        Spacer(Modifier.weight(1f))
    }

    DisposableEffect(Unit) {
        authVM.clearErrorMessages()
        onDispose { authVM.clearErrorMessages() }
    }
}