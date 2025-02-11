package com.example.a24hberlin.ui.screens.auth.nestedcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.buttons.AuthTextButton
import com.example.a24hberlin.ui.screens.components.buttons.LargeDarkButton
import com.example.a24hberlin.ui.screens.components.images.AppLogo
import com.example.a24hberlin.ui.screens.components.textfields.EmailField
import com.example.a24hberlin.ui.screens.components.textfields.PasswordField
import com.example.a24hberlin.ui.viewmodel.AuthViewModel
import com.example.a24hberlin.utils.errorPadding
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.maxPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun RegisterScreen(onClick: () -> Unit) {
    val authVM: AuthViewModel = viewModel()
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.padding(horizontal = regularPadding)) {
            Spacer(Modifier.height(maxPadding))

            Text(
                "Twenty Four Hours Kulturprogramm",
                maxLines = 2,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(largePadding))

            AppLogo()

            Spacer(Modifier.height(largePadding))

            EmailField(
                "Email",
                "Please enter your Email",
                email
            ) { email = it }

            Spacer(Modifier.height(regularPadding))

            PasswordField(
                "Password",
                "Please enter your Password",
                password
            ) { password = it }

            Spacer(Modifier.height(regularPadding))

            PasswordField(
                "Confirm Password",
                "Please confirm your Password",
                confirmPassword
            ) { confirmPassword = it }

            authVM.errorMessage?.let { error ->
                Text(
                    error,
                    Modifier.padding(top = errorPadding)
                )
            }

            LargeDarkButton("Register") { authVM.register(email, password, confirmPassword) }

            Spacer(Modifier.weight(1f))

            Text(
                "Already have an account?",
                Modifier.fillMaxWidth(),
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            AuthTextButton(
                label = "Login",
                onClick = onClick
            )

            Spacer(Modifier.height(largePadding))
        }
    }
}