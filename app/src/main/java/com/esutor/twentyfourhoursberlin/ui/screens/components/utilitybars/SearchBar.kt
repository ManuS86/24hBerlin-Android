package com.esutor.twentyfourhoursberlin.ui.screens.components.utilitybars

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.OutlinedTextFieldDefaults.DecorationBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.theme.microPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onSearchClosed: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(end = regularPadding)
            .padding(bottom = microPadding),
        textStyle = typography.bodyLarge.copy(color = White),
        cursorBrush = SolidColor(White),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = Search, keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        decorationBox = { innerTextField ->
            DecorationBox(
                value = searchText.text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = None,
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_event_names),
                        style = typography.bodyLarge,
                        color = Gray
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        onSearchClosed()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.search),
                            tint = White
                        )
                    }
                },
                trailingIcon = {
                    if (searchText.text.isNotEmpty()) {
                        IconButton(onClick = { onSearchTextChanged(TextFieldValue("")) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.clear_search),
                                tint = White
                            )
                        }
                    }
                },
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                container = {
                    Container(
                        enabled = true,
                        isError = false,
                        interactionSource = remember { MutableInteractionSource() },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Gray.copy(0.15f),
                            unfocusedContainerColor = Gray.copy(0.15f),
                            focusedBorderColor = Transparent,
                            unfocusedBorderColor = Transparent,
                        ),
                        shape = RoundedCornerShape(32.dp),
                    )
                }
            )
        }
    )
}