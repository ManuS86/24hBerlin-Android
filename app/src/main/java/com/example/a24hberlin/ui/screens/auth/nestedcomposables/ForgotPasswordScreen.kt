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
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.extraLargePadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun ForgotPasswordScreen(onClick: () -> Unit) {
    val view = LocalView.current
    val authVM: AuthViewModel = viewModel()
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }

    val confirmationMessage by authVM.confirmationMessage.collectAsStateWithLifecycle()
    val errorMessage by authVM.errorMessage.collectAsStateWithLifecycle()
    val firebaseError by authVM.firebaseError.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = regularPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.twenty_four_hours_kulturprogramm),
            maxLines = 2,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.weight(0.2f))

        AppLogo()

        Spacer(Modifier.weight(0.2f))

        EmailField(
            stringResource(R.string.email),
            stringResource(R.string.enter_your_email),
            email
        ) { email = it }

        if (confirmationMessage != null) {
            Text(
                stringResource(confirmationMessage!!),
                Modifier.padding(top = errorPadding),
                color = Color.Green,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (errorMessage != null) {
            Text(
                stringResource(errorMessage!!),
                Modifier.padding(top = errorPadding),
                color = Color.Red,
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

        LargeDarkButton(stringResource(R.string.reset_password)) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            authVM.resetPassword(email)
        }

        Spacer(Modifier.height(extraLargePadding))

        AuthTextButton(
            stringResource(R.string.try_logging_in_again)
        ) {
            onClick()
        }

        Spacer(Modifier.weight(1f))
    }

    DisposableEffect(Unit) {
        onDispose {
            authVM.clearErrorMessages()
        }
    }
}