package com.example.a24hberlin.ui.screens.components.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.theme.smallPadding

@Composable
fun EmailField(
    label: String,
    placeholder: String,
    email: String,
    onEmailChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Start
    ) {
        Text(
            label,
            fontWeight = SemiBold,
            modifier = Modifier.padding(bottom = smallPadding),
            color = Black
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChanged,
            placeholder = {
                Text(
                    placeholder,
                    color = Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = Email),
            singleLine = true,
            shape = RoundedCornerShape(slightRounding),
            colors = colors(
                focusedBorderColor = Gray.copy(0.5f),
                unfocusedBorderColor = Gray.copy(0.5f),
                focusedContainerColor = White,
                unfocusedContainerColor = White
            )
        )
    }
}