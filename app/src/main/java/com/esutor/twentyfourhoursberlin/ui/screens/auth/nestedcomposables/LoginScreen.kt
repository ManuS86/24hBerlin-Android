package com.esutor.twentyfourhoursberlin.ui.screens.auth.nestedcomposables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.AuthTextButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.buttons.LargeBlackButton
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthPrompt
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.AuthTextField
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.TitleHeader
import com.esutor.twentyfourhoursberlin.ui.viewmodel.AuthViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.doublePadding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding

@Suppress("AssignedValueIsNeverRead")
@Composable
fun LoginScreen(
    authVM: AuthViewModel,
    onClick: () -> Unit
) {
    val errorMessageResId by authVM.errorMessageResId.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showForgotPassword by rememberSaveable { mutableStateOf(false) }

    AnimatedContent(
        targetState = showForgotPassword,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
            } else {
                (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
            }.using(SizeTransform(clip = false))
        },
        label = "AuthScreenTransition"
    ) { isForgotState ->
        if (isForgotState) {
            ForgotPasswordScreen(authVM) { showForgotPassword = false }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = standardPadding)
                    .padding(top = smallPadding),
                horizontalAlignment = CenterHorizontally
            ) {
                TitleHeader(Modifier.height(standardPadding))

                AuthTextField(
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.enter_your_email),
                    value = email,
                    onValueChange = { email = it },
                    isPasswordField = false
                )

                Spacer(Modifier.height(smallPadding))

                AuthTextField(
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.enter_your_password),
                    value = password,
                    onValueChange = { password = it },
                    isPasswordField = true
                )

                if (errorMessageResId != null) {
                    ErrorMessage(errorMessageResId!!)
                }

                LargeBlackButton(stringResource(R.string.login)) { authVM.login(email, password) }
                Spacer(Modifier.height(doublePadding))
                AuthTextButton(stringResource(R.string.forgot_password)) {
                    showForgotPassword = true
                }
                Spacer(Modifier.weight(1f))
                AuthPrompt(R.string.dont_have_an_account, R.string.create_account, onClick)
                Spacer(Modifier.height(smallPadding))
            }
        }

        DisposableEffect(Unit) {
            onDispose { authVM.clearErrorMessages() }
        }
    }
}

@Composable
private fun ErrorMessage(errorMessageResId: Int) {
    Text(
        text = stringResource(errorMessageResId),
        modifier = Modifier.padding(top = smallPadding),
        color = Red,
        style = typography.bodyMedium
    )
}