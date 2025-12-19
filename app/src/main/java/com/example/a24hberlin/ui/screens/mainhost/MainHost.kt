package com.example.a24hberlin.ui.screens.mainhost

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.a24hberlin.ui.theme.Offline
import com.example.a24hberlin.ui.theme.Online
import com.example.a24hberlin.ui.theme.mediumPadding
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.viewmodel.ConnectionEvent
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.utils.SetSystemBarColorsToLight
import kotlinx.coroutines.launch

@Composable
fun MainHost(connectivityVM: ConnectivityViewModel = viewModel()) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val appBarTitleResId = remember(currentRoute, navBackStackEntry) {
        if (currentRoute?.startsWith(Screen.ReAuthWrapper.route) == true) {
            val from = navBackStackEntry?.arguments?.getString("from")
            when (from) {
                "email" -> R.string.change_email
                "password" -> R.string.change_password
                else -> R.string.re_authenticate
            }
        } else {
            when (currentRoute) {
                Screen.Events.route -> Screen.Events.titleResId
                Screen.ClubMap.route -> Screen.ClubMap.titleResId
                Screen.MyEvents.route -> Screen.MyEvents.titleResId
                Screen.Settings.route -> Screen.Settings.titleResId
                else -> Screen.Events.titleResId
            } ?: R.string.events
        }
    }

    val currentAppBarTitle = stringResource(id = appBarTitleResId)

    val bottomBarState = remember { mutableStateOf(true) }
    val showSearchBarState = remember { mutableStateOf(false) }
    val searchTextState = remember { mutableStateOf(TextFieldValue("")) }

    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var selectedMonth by remember { mutableStateOf<Month?>(null) }
    var selectedSound by remember { mutableStateOf<String?>(null) }
    var selectedVenue by remember { mutableStateOf<String?>(null) }

    val showSearchBar by showSearchBarState
    val searchText by searchTextState

    LaunchedEffect(Unit) {
        connectivityVM.connectionEvent.collect { event ->
            when (event) {
                is ConnectionEvent.LostConnection -> {
                    launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.no_internet_connection),
                            duration = SnackbarDuration.Indefinite,
                            withDismissAction = true
                        )
                    }
                }
                is ConnectionEvent.BackOnline -> {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.back_online),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(currentRoute) {
        bottomBarState.value = currentRoute != Screen.ReAuthWrapper.route
        onDispose {}
    }

    Scaffold(
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
                    Screen.ClubMap.route
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val lostConnMessage = stringResource(R.string.no_internet_connection)
                val isOffline = data.visuals.message == lostConnMessage

                Snackbar(
                    modifier = Modifier.padding(mediumPadding),
                    action = {
                        data.visuals.actionLabel?.let { label ->
                            TextButton(onClick = { data.performAction() }) {
                                Text(label, color = White)
                            }
                        }
                    },
                    containerColor = if (isOffline) Offline else Online,
                    contentColor = White,
                    shape = RoundedCornerShape(slightRounding),
                ) {
                    Text(data.visuals.message)
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                contentScale = FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            NavGraph(
                navController,
                searchText,
                selectedEventType,
                selectedMonth,
                selectedSound,
                selectedVenue,
                bottomBarState
            )
        }
    }

    SetSystemBarColorsToLight(false)
}