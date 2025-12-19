package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun RegisterScreen(onClick: () -> Unit) {
    val authVM: AuthViewModel = viewModel()
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()
    val firebaseError by authVM.firebaseError.collectAsStateWithLifecycle()
    val passwordErrorResId by authVM.passwordErrorResId.collectAsStateWithLifecycle()
    val hasNotificationPermission by authVM.hasNotificationPermission.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            authVM.updateNotificationPermission(isGranted)
            authVM.register(email, password, confirmPassword)
        }
    )

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
            placeholder = stringResource(R.string.enter_an_email),
            email = email,
            onEmailChanged = { email = it }
        )

        Spacer(Modifier.height(halfPadding))

        PasswordField(
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.create_a_password),
            password = password,
            onPasswordChanged = { password = it }
        )

        Spacer(Modifier.height(halfPadding))

        PasswordField(
            label = stringResource(R.string.confirm_password),
            placeholder = stringResource(R.string.confirm_your_password),
            password = confirmPassword,
            onPasswordChanged = { confirmPassword = it }
        )

        RegisterErrorMessages(
            errorMessageResId = errorMessageResId,
            passwordMessageResId = passwordErrorResId,
            firebaseError = firebaseError
        )

        LargeDarkButton(
            label = stringResource(R.string.register),
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    authVM.register(email, password, confirmPassword)
                }
            }
        )

        Spacer(Modifier.weight(1f))

        LoginPrompt(onClick)

        Spacer(Modifier.height(halfPadding))
    }

    DisposableEffect(Unit) {
        onDispose {
            authVM.clearErrorMessages()
        }
    }
}

@Composable
private fun RegisterErrorMessages(
    errorMessageResId: Int?,
    passwordMessageResId: Int?,
    firebaseError: String?
) {
    val message = when {
        errorMessageResId != null -> stringResource(errorMessageResId)
        passwordMessageResId != null -> stringResource(passwordMessageResId)
        firebaseError != null -> firebaseError
        else -> null
    }

    message?.let {
        Text(
            text = it,
            modifier = Modifier.padding(top = errorPadding),
            color = Red,
            style = typography.bodyMedium
        )
    }
}

@Composable
private fun LoginPrompt(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.already_have_an_account),
        color = Gray,
        textAlign = Center
    )

    AuthTextButton(
        label = stringResource(R.string.login),
        onClick = onClick
    )
}