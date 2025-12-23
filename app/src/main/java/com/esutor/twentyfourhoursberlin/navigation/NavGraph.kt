package com.esutor.twentyfourhoursberlin.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.esutor.twentyfourhoursberlin.ui.screens.clubmap.ClubMapScreen
import com.esutor.twentyfourhoursberlin.ui.screens.events.EventsScreen
import com.esutor.twentyfourhoursberlin.ui.screens.myevents.MyEventsScreen
import com.esutor.twentyfourhoursberlin.ui.screens.settings.SettingsScreen
import com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.ReAuthWrapper
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
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
            EventsScreen(eventVM, connectivityVM)
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
            MyEventsScreen(eventVM, connectivityVM)
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            SettingsScreen(navController, settingsVM, eventVM)
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