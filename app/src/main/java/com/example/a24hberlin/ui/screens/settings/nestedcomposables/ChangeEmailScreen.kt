package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.extraLargePadding
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun ChangeEmailScreen() {
    val settingsVM: SettingsViewModel = viewModel()
    var email by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            settingsVM.clearErrorMessages()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.padding(horizontal = regularPadding)) {
            Spacer(Modifier.padding(extraLargePadding))

            Text(
                "Change your Email",
                Modifier.padding(top = largePadding),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.weight(0.5f))

            EmailField(
                "New Email",
                "Please enter your new email",
                email
            ) { email = it }

            if (settingsVM.errorMessage.isNotEmpty()) {
                Text(
                    settingsVM.errorMessage,
                    Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (settingsVM.confirmationMessage.isNotEmpty()) {
                Text(
                    settingsVM.confirmationMessage,
                    Modifier.padding(top = errorPadding),
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LargeDarkButton("Change Email") {
                settingsVM.changeEmail(email)
            }

            Spacer(Modifier.weight(1f))
        }
    }
}