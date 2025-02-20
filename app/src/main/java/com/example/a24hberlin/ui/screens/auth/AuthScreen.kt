package com.example.a24hberlin.ui.screens.auth

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
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.LoginScreen
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.RegisterScreen

@Composable
fun AuthScreen(innerPadding: PaddingValues) {
    var showRegister by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (showRegister) {
            RegisterScreen { showRegister = !showRegister }
        } else {
            LoginScreen { showRegister = !showRegister }
        }
    }
}