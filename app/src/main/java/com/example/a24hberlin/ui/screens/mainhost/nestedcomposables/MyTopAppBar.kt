package com.example.a24hberlin.ui.screens.mainhost.nestedcomposables

import android.view.SoundEffectConstants
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.example.a24hberlin.R
import com.example.a24hberlin.navigation.Screen
import com.example.a24hberlin.ui.screens.components.utilitybars.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    currentRoute: String?,
    showSearchBar: Boolean,
    searchText: TextFieldValue,
    onSearchIconClick: () -> Unit,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onSearchClosed: () -> Unit,
    navController: NavHostController
) {
    val view = LocalView.current

    val backButtonRoutes = remember {
        setOf(
            Screen.ReAuthWrapper("").route
        )
    }

    val hideSearchRoutes = remember {
        setOf(
            Screen.Settings.route,
            Screen.ReAuthWrapper("").route
        )
    }

    val showBackButton = currentRoute in backButtonRoutes
    val showSearchComponent = currentRoute !in hideSearchRoutes && !showBackButton

    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (showSearchComponent) {
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
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}