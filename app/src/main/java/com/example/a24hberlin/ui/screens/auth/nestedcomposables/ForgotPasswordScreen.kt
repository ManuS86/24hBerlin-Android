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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.images.AppLogo
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.extraLargePadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun ForgotPasswordScreen() {
    val authVM: AuthViewModel = viewModel()
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }

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
                "Twenty Four Hours Kulturprogramm",
                maxLines = 2,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(regularPadding))

            AppLogo()

            Spacer(Modifier.height(regularPadding))

            EmailField(
                "Email",
                "Please enter your Email",
                email
            ) { email = it }

            if (authVM.errorMessage.isNotEmpty()) {
                Text(
                    authVM.errorMessage,
                    Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (authVM.confirmationMessage.isNotEmpty()) {
                Text(
                    authVM.confirmationMessage,
                    Modifier.padding(top = errorPadding),
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LargeDarkButton("Reset Password") {
                authVM.resetPassword(email)
            }
        }
    }
}