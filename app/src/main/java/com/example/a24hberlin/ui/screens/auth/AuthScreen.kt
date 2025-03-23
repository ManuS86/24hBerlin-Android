package com.example.a24hberlin.ui.screens.auth

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.LoginScreen
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.RegisterScreen

@Composable
fun AuthScreen(innerPadding: PaddingValues) {
    val view = LocalView.current
    var showRegister by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (showRegister) {
            RegisterScreen {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                showRegister = !showRegister
            }
        } else {
            LoginScreen {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                showRegister = !showRegister
            }
        }
    }
}