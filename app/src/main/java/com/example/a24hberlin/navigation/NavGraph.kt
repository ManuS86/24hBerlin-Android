package com.example.a24hberlin.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a24hberlin.ui.screens.clubmap.ClubMapScreen
import com.example.a24hberlin.ui.screens.events.EventsScreen
import com.example.a24hberlin.ui.screens.myevents.MyEventsScreen
import com.example.a24hberlin.ui.screens.settings.SettingsScreen
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.ReAuthWrapper
import com.example.a24hberlin.ui.viewmodel.ConnectivityViewModel
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    connectivityVM: ConnectivityViewModel,
    eventVM: EventViewModel,
    settingsVM: SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Events.route
    ) {
        composable(
            route = Screen.Events.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            EventsScreen(connectivityVM, eventVM)
        }
        composable(
            Screen.ClubMap.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ClubMapScreen(eventVM)
        }
        composable(
            route = Screen.MyEvents.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            MyEventsScreen(connectivityVM, eventVM)
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            SettingsScreen(navController, bottomBarState, eventVM, settingsVM)
        }
        composable(
            route = Screen.ReAuthWrapper.route,
            arguments = Screen.ReAuthWrapper.navArguments,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300)) { it }
            },
            exitTransition = {
                ExitTransition.None
            },
            popEnterTransition = {
                EnterTransition.None
            },
            popExitTransition = {
                slideOutHorizontally(animationSpec = tween(300)) { it }
            }
        ) { backStackEntry ->
            val from = backStackEntry.arguments?.getString(Screen.ReAuthWrapper.ARG_FROM) ?: ""

            ReAuthWrapper(from, settingsVM)
        }
    }
}