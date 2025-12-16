package com.example.a24hberlin.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.ui.screens.clubmap.ClubMapScreen
import com.example.a24hberlin.ui.screens.events.EventsScreen
import com.example.a24hberlin.ui.screens.favorites.FavoritesScreen
import com.example.a24hberlin.ui.screens.settings.SettingsScreen
import com.example.a24hberlin.ui.screens.settings.nestedcomposables.ReAuthWrapper

@Composable
fun NavGraph(
    navController: NavHostController,
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: String?,
    selectedVenue: String?,
    bottomBarState: MutableState<Boolean>,
    onSetTitleId: (Int?) -> Unit
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
            EventsScreen(searchText, selectedEventType, selectedMonth, selectedSound, selectedVenue)
        }
        composable(
            Screen.ClubMap.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ClubMapScreen(searchText, selectedEventType, selectedMonth, selectedSound, selectedVenue)
        }
        composable(
            route = Screen.Favorites.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            FavoritesScreen(searchText, selectedEventType, selectedMonth, selectedSound, selectedVenue)
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            SettingsScreen(navController, bottomBarState)
        }
        composable(
            route = Screen.ReAuthWrapper.route,
            arguments = Screen.ReAuthWrapper.arguments,
            enterTransition = { slideInHorizontally(animationSpec = tween(300)) { it } },
            popExitTransition = { slideOutHorizontally(animationSpec = tween(300)) { it } }
        ) { backStackEntry ->
            val from = backStackEntry.arguments?.getString(Screen.ReAuthWrapper.ARGUMENT_KEY) ?: ""

            ReAuthWrapper(from, onSetTitleId)
        }
    }
}