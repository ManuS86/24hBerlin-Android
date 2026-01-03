package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements.PasswordReAuthForm
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel

@Composable
fun ReAuthWrapper(
    from: String,
    settingsVM: SettingsViewModel
) {
    val firebaseError by settingsVM.firebaseError.collectAsStateWithLifecycle()
    val isReauthenticated by settingsVM.isReauthenticated.collectAsStateWithLifecycle()

    var password by rememberSaveable { mutableStateOf("") }

    DisposableEffect(Unit) { onDispose { settingsVM.clearErrorMessages() } }

    Box(Modifier.fillMaxSize()) {
        Background()

        AnimatedContent(
            targetState = isReauthenticated,
            transitionSpec = {
                if (targetState) {
                    (slideInHorizontally { it } + fadeIn())
                        .togetherWith(slideOutHorizontally { -it } + fadeOut())
                } else {
                    (slideInHorizontally { -it } + fadeIn())
                        .togetherWith(slideOutHorizontally { it } + fadeOut())
                }.using(SizeTransform(clip = false))
            },
            label = "ReAuthTransition"
        ) { authenticated ->
            if (authenticated) {
                if (from == "email") ChangeEmailScreen(settingsVM) else ChangePasswordScreen(settingsVM)
            } else {
                PasswordReAuthForm(
                    settingsVM = settingsVM,
                    firebaseError = firebaseError,
                    password = password,
                    onPasswordChange = { password = it }
                )
            }
        }
    }
}