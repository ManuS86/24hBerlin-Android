package com.example.a24hberlin.ui.screens.settings.nestedcomposables

import android.view.SoundEffectConstants
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun ChangeEmailScreen() {
    val view = LocalView.current
    val settingsVM: SettingsViewModel = viewModel()

    var email by remember { mutableStateOf("") }

    val confirmationMessage by settingsVM.confirmationMessage.collectAsStateWithLifecycle()
    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.padding(horizontal = regularPadding)) {
            Spacer(Modifier.weight(0.7f))

            Text(
                text = stringResource(R.string.change_your_email),
                modifier = Modifier.padding(vertical = largePadding),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            EmailField(
                label = stringResource(R.string.new_email),
                placeholder = stringResource(R.string.enter_your_new_email),
                email = email,
                onEmailChanged = { email = it }
            )

            if (confirmationMessage != null) {
                Text(
                    text = stringResource(confirmationMessage!!),
                    modifier = Modifier.padding(top = errorPadding),
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (firebaseError != null) {
                Text(
                    text = firebaseError!!,
                    modifier = Modifier.padding(top = errorPadding),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LargeDarkButton(
                label = stringResource(R.string.change_email),
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    settingsVM.changeEmail(email)
                }
            )

            Spacer(Modifier.weight(1f))
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            settingsVM.clearErrorMessages()
        }
    }
}