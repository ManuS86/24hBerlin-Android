package com.esutor.twentyfourhoursberlin.ui.screens.auth.nestedcomposables

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
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding

@Composable
fun RegisterScreen(
    authVM: AuthViewModel,
    onClick: () -> Unit
) {
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()
    val firebaseError by authVM.firebaseError.collectAsStateWithLifecycle()
    val passwordErrorResId by authVM.passwordErrorResId.collectAsStateWithLifecycle()
    val hasNotificationPermission by authVM.hasNotificationPermission.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            authVM.updateNotificationPermission(isGranted)
            authVM.executeRegistration(email, password)
        }
    )

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
            placeholder = stringResource(R.string.enter_an_email),
            value = email,
            onValueChange = { email = it },
            isPasswordField = false
        )

        Spacer(Modifier.height(halfPadding))

        AuthTextField(
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string.create_a_password),
            value = password,
            onValueChange = { password = it },
            isPasswordField = true
        )

        Spacer(Modifier.height(halfPadding))

        AuthTextField(
            label = stringResource(R.string.confirm_password),
            placeholder = stringResource(R.string.confirm_your_password),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPasswordField = true
        )

        RegisterErrorMessages(
            errorMessageResId = errorMessageResId,
            passwordMessageResId = passwordErrorResId,
            firebaseError = firebaseError
        )

        LargeDarkButton(stringResource(R.string.register)) {
            // 1. Start the registration flow
            authVM.register(password, confirmPassword) {
                // 2. This block ONLY runs if checkPassword returned null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // 3. Either permission is granted or not needed; do the network work
                    authVM.executeRegistration(email, password)
                }
            }
        }

        Spacer(Modifier.weight(1f))
        LoginPrompt(onClick)
        Spacer(Modifier.height(halfPadding))
    }

    DisposableEffect(Unit) {
        onDispose { authVM.clearErrorMessages() }
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

    AuthTextButton(stringResource(R.string.login), onClick)
}