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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.textfields.AuthMessages
import com.example.a24hberlin.ui.screens.components.buttons.AuthTextButton
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.images.AppLogo
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.ui.theme.extraLargePadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun ForgotPasswordScreen(onClick: () -> Unit) {
    val authVM: AuthViewModel = viewModel()
    val confirmationMessageResId by authVM.confirmationMessageResId.collectAsStateWithLifecycle()
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()
    val firebaseError by authVM.firebaseError.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = regularPadding),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.twenty_four_hours_kulturprogramm),
            maxLines = 2,
            fontWeight = Black,
            textAlign = Center,
            style = typography.headlineLarge
        )

        Spacer(Modifier.weight(0.2f))

        AppLogo()

        Spacer(Modifier.weight(0.2f))

        EmailField(
            label = stringResource(R.string.email),
            placeholder = stringResource(R.string.enter_your_email),
            email = email,
            onEmailChanged = { email = it }
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

        Spacer(Modifier.height(extraLargePadding))

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