package com.esutor.twentyfourhoursberlin.ui.screens.auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.esutor.twentyfourhoursberlin.di.ViewModelFactoryHelper
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.MainHost
import com.esutor.twentyfourhoursberlin.ui.viewmodel.AuthViewModel
import com.esutor.twentyfourhoursberlin.utils.SetSystemBarColorsToLight

@Composable
fun AuthWrapper(innerPadding: PaddingValues) {
    val authVM: AuthViewModel =
        viewModel(factory = ViewModelFactoryHelper.provideAuthViewModelFactory())
    val currentUser by authVM.currentUser.collectAsStateWithLifecycle()

    if (currentUser != null) {
        MainHost()
    } else {
        SetSystemBarColorsToLight(true)
        AuthScreen(authVM, innerPadding)
    }
}