package com.example.a24hberlin.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.LoginScreen
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.RegisterScreen
import com.example.a24hberlin.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen() {
    val authVM: AuthViewModel = viewModel()

    if (authVM.showRegister) {
        RegisterScreen()
    } else {
        LoginScreen()
    }
}