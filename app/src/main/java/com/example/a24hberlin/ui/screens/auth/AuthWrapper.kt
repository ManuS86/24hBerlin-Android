package com.example.a24hberlin.ui.screens.auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.ui.screens.mainhost.AppNavigation
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.SetSystemBarColorsToLight

@Composable
fun AuthWrapper(innerPadding: PaddingValues) {
    val authVM: AuthViewModel = viewModel()
    val currentUser by authVM.currentUser.collectAsState()

    if (currentUser != null) {
        AppNavigation()
    } else {
        SetSystemBarColorsToLight(true)
        AuthScreen(innerPadding)
    }
}