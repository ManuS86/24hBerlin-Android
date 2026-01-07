package com.esutor.twentyfourhoursberlin.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.esutor.twentyfourhoursberlin.ui.screens.auth.nestedcomposables.LoginScreen
import com.esutor.twentyfourhoursberlin.ui.screens.auth.nestedcomposables.RegisterScreen
import com.esutor.twentyfourhoursberlin.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authVM: AuthViewModel,
    innerPadding: PaddingValues
) {
    var showLogin by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        AnimatedContent(
            targetState = showLogin,
            transitionSpec = {
                if (targetState) {
                    (slideInHorizontally { width -> width } + fadeIn())
                        .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn())
                        .togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                }.using(
                    SizeTransform(clip = false)
                )
            },
            label = "AuthScreenTransition"
        ) { targetShowRegister ->
            if (targetShowRegister) {
                LoginScreen(authVM) {
                    authVM.clearErrorMessages()
                    showLogin = false
                }
            } else {
                RegisterScreen(authVM) {
                    authVM.clearErrorMessages()
                    showLogin = true
                }
            }
        }
    }
}