package com.example.a24hberlin.ui.screens.auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.ui.screens.mainhost.AppNavigation
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.SetSystemBarColorsToLight

@Composable
fun AuthWrapper(innerPadding: PaddingValues) {
    val authVM: AuthViewModel = viewModel()

    if (authVM.currentUser != null) {
        AppNavigation()
    } else {
        SetSystemBarColorsToLight(true)
        AuthScreen(innerPadding)
    }
}