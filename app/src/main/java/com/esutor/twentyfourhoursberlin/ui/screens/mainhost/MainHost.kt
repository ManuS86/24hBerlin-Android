package com.esutor.twentyfourhoursberlin.ui.screens.mainhost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.di.ViewModelFactoryHelper
import com.esutor.twentyfourhoursberlin.navigation.NavGraph
import com.esutor.twentyfourhoursberlin.navigation.Screen
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilitybars.FilterBar
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.ScheduleReminderEffect
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.LoadingOverlay
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.connectivitysnackbar.ConnectivitySnackbarHost
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.MainBottomNavigationBar
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.MainTopAppBar
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.connectivitysnackbar.ConnectivitySnackbarEffect
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import com.esutor.twentyfourhoursberlin.utils.SetSystemBarColorsToLight

@Composable
fun MainHost() {
// --- ViewModels & Controllers ---
    val haptic = LocalHapticFeedback.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val connectivityVM: ConnectivityViewModel = viewModel(factory = ViewModelFactoryHelper.provideConnectivityViewModelFactory())
    val eventVM: EventViewModel = viewModel(factory = ViewModelFactoryHelper.provideEventViewModelFactory())
    val settingsVM: SettingsViewModel = viewModel(factory = ViewModelFactoryHelper.provideSettingsViewModelFactory())

    // --- Navigation & UI State ---
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mainTabRoutes = remember {
        Screen.allScreens.filter { it.titleResId == R.string.app_name }.map { it.route }
    }
    val isMainTab = remember(currentRoute) { currentRoute in mainTabRoutes }
    val showFilterBar = remember(currentRoute) {
        currentRoute in listOf(Screen.Events.route, Screen.ClubMap.route)
    }
    val loadingProgress by eventVM.loadingProgress.collectAsStateWithLifecycle()
    var showLoadingScreen by rememberSaveable { mutableStateOf(true) }

    val showSearchBarState = rememberSaveable { mutableStateOf(false) }
    val showSearchBar by showSearchBarState

    val closeSearch = {
        haptic.performHapticFeedback(TextHandleMove)
        showSearchBarState.value = false
        eventVM.updateSearchText(TextFieldValue(""))
    }

    // --- Title Logic ---
    val appBarTitleResId = remember(currentRoute, navBackStackEntry, isMainTab) {
        when {
            isMainTab -> R.string.app_name
            currentRoute?.startsWith(Screen.ReAuthWrapper.route.substringBefore("/")) == true -> {
                when (navBackStackEntry?.arguments?.getString(Screen.ReAuthWrapper.ARG_FROM)) {
                    "email" -> R.string.change_email
                    "password" -> R.string.change_password
                    else -> R.string.re_authenticate
                }
            }
            else -> Screen.fromRoute(currentRoute)?.titleResId ?: R.string.events
        }
    }

    // --- Side Effects ---
    SetSystemBarColorsToLight(false)
    ConnectivitySnackbarEffect(connectivityVM, snackbarHostState)
    ScheduleReminderEffect(eventVM)

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Column {
                    MainTopAppBar(
                        title = stringResource(appBarTitleResId),
                        currentRoute = currentRoute,
                        showSearchBar = showSearchBar,
                        onSearchIconClick = {
                            haptic.performHapticFeedback(TextHandleMove)
                            showSearchBarState.value = !showSearchBarState.value
                        },
                        onSearchClosed = closeSearch,
                        navController = navController,
                        eventVM = eventVM
                    )

                    if (showFilterBar) {
                        FilterBar(eventVM)
                    }
                }
            },
            bottomBar = {
                if (isMainTab) {
                    MainBottomNavigationBar(
                        navController = navController,
                        onTabSelected = closeSearch
                    )
                }
            },
            snackbarHost = { ConnectivitySnackbarHost(snackbarHostState) },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { paddingValues ->
            Surface(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Background()

                NavGraph(
                    navController = navController,
                    connectivityVM = connectivityVM,
                    eventVM = eventVM,
                    settingsVM = settingsVM
                )
            }
        }

        if (showLoadingScreen) {
            LoadingOverlay(progressValue = loadingProgress) { showLoadingScreen = false }
        }
    }
}