package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import android.Manifest
import android.os.Build
import android.view.SoundEffectConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.a24hberlin.ui.viewmodel.PermissionViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun RegisterScreen(onClick: () -> Unit) {
    val view = LocalView.current
    val authVM: AuthViewModel = viewModel()
    val permissionVM: PermissionViewModel = viewModel()
    val scrollState = rememberScrollState()
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by authVM.errorMessage.collectAsStateWithLifecycle()
    val firebaseError by authVM.firebaseError.collectAsStateWithLifecycle()
    val passwordError by authVM.passwordError.collectAsStateWithLifecycle()
    val hasNotificationPermission by permissionVM.hasNotificationPermission.collectAsStateWithLifecycle()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionVM.updateNotificationPermission(isGranted)
        }
    )

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

        Spacer(Modifier.height(regularPadding))

        AppLogo()

        Spacer(Modifier.height(regularPadding))

        EmailField(
            stringResource(R.string.email),
            stringResource(R.string.enter_an_email),
            email
        ) { email = it }

        Spacer(Modifier.height(mediumPadding))

        PasswordField(
            stringResource(R.string.password),
            stringResource(R.string.create_a_password),
            password
        ) { password = it }

        Spacer(Modifier.height(mediumPadding))

        PasswordField(
            stringResource(R.string.confirm_password),
            stringResource(R.string.confirm_your_password),
            confirmPassword
        ) { confirmPassword = it }

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

        if (passwordError != null) {
            Text(
                stringResource(passwordError!!),
                Modifier.padding(top = errorPadding),
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LargeDarkButton(stringResource(R.string.register)) {
            view.playSoundEffect(SoundEffectConstants.CLICK)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            authVM.register(email, password, confirmPassword)
        }

        Spacer(Modifier.weight(1f))

        Text(
            stringResource(R.string.already_have_an_account),
            Modifier.fillMaxWidth(),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        AuthTextButton(
            stringResource(R.string.login),
            onClick = onClick
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            authVM.clearErrorMessages()
        }
    }
}