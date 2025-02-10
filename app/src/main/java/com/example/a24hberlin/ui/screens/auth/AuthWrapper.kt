package com.example.a24hberlin.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.ui.screens.appnavigation.AppNavigation
import com.example.a24hberlin.ui.viewmodel.AuthViewModel

@Composable
fun AuthWrapper() {
    val authVM: AuthViewModel = viewModel()

    if (authVM.currentUser != null) {
        AppNavigation()
    } else {
        AuthScreen()
    }
}