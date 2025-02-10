package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.screens.components.textfields.PasswordField
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.logoSize
import com.example.a24hberlin.utils.mediumPadding

@Composable
fun LoginScreen() {
    val authVM: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Text(
            "Twenty Four Hours Kulturprogramm",
            Modifier.padding(top = largePadding),
            maxLines = 2,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(logoSize)
        )

        Column(horizontalAlignment = Alignment.Start) {
            EmailField(
                email,
                "Email",
                "Please enter your Email"
            ) { email = it }

            PasswordField(
                password,
                "Password",
                "Please enter your Password"
            ) { password = it }

            authVM.errorMessage?.let { error ->
                Text(
                    error,
                    Modifier.padding(top = errorPadding)
                )
            }

            LargeDarkButton("Login") { authVM.login(email, password) }

            Column {
                Text(
                    "Don't have an account?",
                    Modifier.padding(bottom = mediumPadding),
                    color = Color.Gray
                )
            }
        }
    }
}