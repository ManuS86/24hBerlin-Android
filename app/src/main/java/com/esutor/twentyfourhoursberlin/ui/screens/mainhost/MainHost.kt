package com.esutor.twentyfourhoursberlin.ui.screens.mainhost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides.Companion.Horizontal
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.LoadingScreen
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.connectivitysnackbar.ConnectivitySnackbarHost
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.MainBottomNavigationBar
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.MainTopAppBar
import com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.connectivitysnackbar.ConnectivitySnackbarEffect
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import com.esutor.twentyfourhoursberlin.utils.SetSystemBarColorsToLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHost() {
// --- ViewModels & Controllers ---
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val connectivityVM: ConnectivityViewModel = viewModel(factory = ViewModelFactoryHelper.provideConnectivityViewModelFactory())
    val eventVM: EventViewModel = viewModel(factory = ViewModelFactoryHelper.provideEventViewModelFactory())
    val settingsVM: SettingsViewModel = viewModel(factory = ViewModelFactoryHelper.provideSettingsViewModelFactory())

    // --- Navigation & UI State ---
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isMainTab by remember(currentRoute) {
        derivedStateOf {
            Screen.allScreens.any { it.route == currentRoute && it.titleResId == R.string.app_name }
        }
    }
    val showFilterBar = currentRoute == Screen.Events.route || currentRoute == Screen.ClubMap.route
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val isFinished by eventVM.isLoadingFinished.collectAsStateWithLifecycle()

    val isVisible by remember { derivedStateOf { !isFinished } }

    val showSearchBarState = rememberSaveable { mutableStateOf(false) }
    val showSearchBar by showSearchBarState

    val closeSearch = {
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
    ConnectivitySnackbarEffect(connectivityVM, snackbarHostState)
    ScheduleReminderEffect(eventVM)
    SetSystemBarColorsToLight(false)

    LaunchedEffect(currentRoute) {
        scrollBehavior.state.heightOffset = 0f
        scrollBehavior.state.contentOffset = 0f
    }

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Column(Modifier.windowInsetsPadding(WindowInsets.navigationBars.only(Horizontal))) {
                    MainTopAppBar(
                        title = stringResource(appBarTitleResId),
                        scrollBehavior = scrollBehavior,
                        currentRoute = currentRoute,
                        showSearchBar = showSearchBar,
                        onSearchIconClick = { showSearchBarState.value = !showSearchBarState.value },
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
                    Column(Modifier.windowInsetsPadding(WindowInsets.navigationBars.only(Horizontal))) {
                        MainBottomNavigationBar(
                            navController = navController,
                            onTabSelected = closeSearch
                        )
                    }
                }
            },
            snackbarHost = { ConnectivitySnackbarHost(snackbarHostState) },
            contentWindowInsets = WindowInsets.navigationBars
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

        LoadingScreen(isVisible)
    }
}