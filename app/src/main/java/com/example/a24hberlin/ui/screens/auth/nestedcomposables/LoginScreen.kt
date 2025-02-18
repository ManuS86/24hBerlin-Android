package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.R
import com.example.a24hberlin.navigation.Screen
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
    val authVM: AuthViewModel = viewModel()
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            authVM.clearErrorMessages()
        }
    }

    Box {
        Image(
            painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(regularPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(extraLargePadding))

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
                stringResource(R.string.please_enter_your_email),
                email
            ) { email = it }

            Spacer(Modifier.height(mediumPadding))

            PasswordField(
                stringResource(R.string.password),
                stringResource(R.string.please_confirm_your_password),
                password
            ) { password = it }

            if (authVM.errorMessage != null) {
                Text(
                    stringResource(authVM.errorMessage!!),
                    Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LargeDarkButton(stringResource(R.string.login)) {
                authVM.login(email, password)
            }

            Spacer(Modifier.height(regularPadding))

            AuthTextButton(
                stringResource(R.string.forgot_password),
                onClick = { navController.navigate(Screen.ForgotPassword.route) }
            )

            Spacer(Modifier.weight(1f))

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
    }
}