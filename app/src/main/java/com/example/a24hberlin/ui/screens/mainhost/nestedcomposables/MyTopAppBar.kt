package com.example.a24hberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.a24hberlin.R
import com.example.a24hberlin.navigation.Screen
import com.example.a24hberlin.ui.screens.components.utilitybars.SearchBar
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    currentRoute: String?,
    showSearchBar: Boolean,
    onSearchIconClick: () -> Unit,
    onSearchClosed: () -> Unit,
    navController: NavHostController,
    eventVM: EventViewModel
) {
    val haptic = LocalHapticFeedback.current

    val searchTextValue by eventVM.searchTextFieldValue.collectAsStateWithLifecycle()

    val backButtonRoutes = remember {
        setOf(Screen.ReAuthWrapper.route)
    }

    val hideSearchRoutes = remember {
        setOf(
            Screen.Settings.route,
            Screen.ReAuthWrapper.route
        )
    }

    val showBackButton = currentRoute in backButtonRoutes
    val showSearchComponent = currentRoute !in hideSearchRoutes && !showBackButton

    TopAppBar(
        title = {
            if (showSearchBar && showSearchComponent) {
                SearchBar(
                    searchText = searchTextValue,
                    onSearchTextChanged = { eventVM.updateSearchText(it) },
                    onSearchClosed = onSearchClosed
                )
            } else {
                Row(
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(halfPadding)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(color = White, shape = slightRounding),
                        contentAlignment = Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = null
                        )
                    }
                    Text(text = title, fontWeight = SemiBold)
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (showSearchComponent && !showSearchBar) {
                IconButton(onClick = onSearchIconClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }
            }
        },
        colors = topAppBarColors(
            titleContentColor = White,
            navigationIconContentColor = White,
            actionIconContentColor = White
        )
    )
}