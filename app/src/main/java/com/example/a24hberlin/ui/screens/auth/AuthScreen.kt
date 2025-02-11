package com.example.a24hberlin.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.LoginScreen
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.RegisterScreen

@Composable
fun AuthScreen() {
    var showRegister by remember { mutableStateOf(false) }

    if (showRegister) {
        RegisterScreen { showRegister = !showRegister }
    } else {
        LoginScreen { showRegister = !showRegister }
    }
}