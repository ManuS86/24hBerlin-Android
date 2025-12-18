package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.AuthMessages
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.ui.theme.largePadding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun ChangeEmailScreen() {
    val settingsVM: SettingsViewModel = viewModel()
    val confirmationMessageResId by settingsVM.confirmationMessageResId.collectAsStateWithLifecycle()
    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.padding(horizontal = regularPadding)) {
            Spacer(Modifier.weight(0.7f))

            Text(
                text = stringResource(R.string.change_your_email),
                modifier = Modifier.padding(vertical = largePadding),
                fontWeight = Bold,
                style = typography.titleLarge,
                color = Black
            )

            EmailField(
                label = stringResource(R.string.new_email),
                placeholder = stringResource(R.string.enter_your_new_email),
                email = email,
                onEmailChanged = { email = it }
            )

            AuthMessages(
                confirmationMessageResId = confirmationMessageResId,
                firebaseError = firebaseError
            )

            LargeDarkButton(
                label = stringResource(R.string.change_email),
                onClick = { settingsVM.changeEmail(email) }
            )

            Spacer(Modifier.weight(1f))
        }
    }

    DisposableEffect(Unit) { onDispose { settingsVM.clearErrorMessages() } }
}