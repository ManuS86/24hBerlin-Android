package com.example.a24hberlin.navigation

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
    onTitleChange: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Events.route
    ) {
        composable(Screen.Events.route) {
            EventsScreen(
                searchText,
                selectedEventType,
                selectedMonth,
                selectedSound,
                selectedVenue
            )
        }
        composable(Screen.ClubMap.route) {
            ClubMapScreen(
                searchText,
                selectedEventType,
                selectedMonth,
                selectedSound,
                selectedVenue
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                searchText,
                selectedEventType,
                selectedMonth,
                selectedSound,
                selectedVenue
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                navController,
                bottomBarState
            )
        }
        composable(
            route = Screen.ReAuthWrapper("").route,
            arguments = Screen.ReAuthWrapper("").arguments
        ) { backStackEntry ->
            val from = backStackEntry.arguments?.getString("from") ?: ""
            ReAuthWrapper(from, onTitleChange)
        }
    }
}
