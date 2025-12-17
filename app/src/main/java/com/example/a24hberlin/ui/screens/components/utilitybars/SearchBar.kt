package com.example.a24hberlin.ui.screens.components.utilitybars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.R
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun SearchBar(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onSearchClosed: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        leadingIcon = {
            IconButton(onClick = {
                haptic.performHapticFeedback(TextHandleMove)
                onSearchClosed()
            }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        trailingIcon = {
            if (searchText.text.isNotEmpty()) {
                IconButton(onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    onSearchTextChanged(TextFieldValue(""))
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.clear_search)
                    )
                }
            }
        },
        placeholder = { Text(stringResource(R.string.search_)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = regularPadding)
            .padding(bottom = mediumPadding),
        singleLine = true,
        shape = RoundedCornerShape(32.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.Gray.copy(0.15f),
            unfocusedContainerColor = Color.Gray.copy(0.15f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray
        )
    )
}