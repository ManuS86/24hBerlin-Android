package com.esutor.twentyfourhoursberlin.ui.screens.auth

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
    var showRegister by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (showRegister) {
            RegisterScreen(authVM) { showRegister = !showRegister }
        } else {
            LoginScreen(authVM) { showRegister = !showRegister }
        }
    }
}