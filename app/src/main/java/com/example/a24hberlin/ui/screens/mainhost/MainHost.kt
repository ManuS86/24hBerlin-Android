package com.example.a24hberlin.ui.screens.mainhost

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.navigation.NavGraph
import com.example.a24hberlin.navigation.Screen
import com.example.a24hberlin.ui.screens.components.utilitybars.FilterBar
import com.example.a24hberlin.ui.screens.mainhost.nestedcomposables.MyBottomNavigationBar
import com.example.a24hberlin.ui.screens.mainhost.nestedcomposables.MyTopAppBar
import com.example.a24hberlin.utils.SetSystemBarColorsToLight

@Composable
fun MainHost() {
    val haptic = LocalHapticFeedback.current
    val navController = rememberNavController()

    val allScreens = remember {
        listOf(
            Screen.Events,
            Screen.ClubMap,
            Screen.Favorites,
            Screen.Settings,
            Screen.ReAuthWrapper
        )
    }

    val appBarTitleState = remember { mutableStateOf(Screen.Events.titleResId) }
    val bottomBarState = remember { mutableStateOf(true) }

    val showSearchBarState = remember { mutableStateOf(false) }
    val searchTextState = remember { mutableStateOf(TextFieldValue("")) }

    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var selectedMonth by remember { mutableStateOf<Month?>(null) }
    var selectedSound by remember { mutableStateOf<String?>(null) }
    var selectedVenue by remember { mutableStateOf<String?>(null) }

    val currentAppBarTitle = appBarTitleState.value?.let { stringResource(id = it) } ?: ""

    val showSearchBar by showSearchBarState
    val searchText by searchTextState

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    DisposableEffect(currentRoute) {
        val currentScreen = allScreens.firstOrNull { it.route == currentRoute }
        val dynamicTitleRoute = Screen.ReAuthWrapper.route

        val isReAuthRoute = currentRoute == dynamicTitleRoute

        if (!isReAuthRoute) {
            appBarTitleState.value = currentScreen?.titleResId
            bottomBarState.value = true
        } else {
            bottomBarState.value = false
        }

        onDispose {}
    }

    val onSetTitleId: (Int?) -> Unit = { resId ->
        appBarTitleState.value = resId
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,

        topBar = {
            Column {
                MyTopAppBar(
                    title = currentAppBarTitle,
                    currentRoute = currentRoute,
                    showSearchBar = showSearchBar,
                    searchText = searchText,
                    onSearchIconClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        showSearchBarState.value = !showSearchBarState.value
                    },
                    onSearchTextChanged = { searchTextState.value = it },
                    onSearchClosed = {
                        haptic.performHapticFeedback(TextHandleMove)
                        showSearchBarState.value = false
                    },
                    navController = navController
                )

                val showFilterBar = currentRoute in listOf(
                    Screen.Events.route,
                    Screen.ClubMap.route,
                    Screen.Favorites.route
                )

                if (showFilterBar) {
                    FilterBar(
                        selectedEventType,
                        { selectedEventType = it },
                        selectedMonth,
                        { selectedMonth = it },
                        selectedSound,
                        { selectedSound = it },
                        selectedVenue,
                        { selectedVenue = it }
                    )
                }
            }
        },

        bottomBar = {
            if (bottomBarState.value) {
                MyBottomNavigationBar(
                    navController,
                    onTabSelected = {
                        showSearchBarState.value = false
                        searchTextState.value = TextFieldValue("")
                    }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            NavGraph(
                navController,
                searchText,
                selectedEventType,
                selectedMonth,
                selectedSound,
                selectedVenue,
                bottomBarState,
                onSetTitleId
            )
        }
    }

    SetSystemBarColorsToLight(false)
}