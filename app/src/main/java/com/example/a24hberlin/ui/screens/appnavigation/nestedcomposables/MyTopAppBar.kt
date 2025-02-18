package com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.screens.components.utilitybars.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    showSearchBar: Boolean,
    searchText: TextFieldValue,
    onSearchIconClick: () -> Unit,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onSearchClosed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        },
        actions = {
            if (title != stringResource(R.string.settings)) {
                if (!showSearchBar) {
                    IconButton(onClick = onSearchIconClick) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                } else {
                    SearchBar(
                        searchText = searchText,
                        onSearchTextChanged = onSearchTextChanged,
                        onSearchClosed = onSearchClosed
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}