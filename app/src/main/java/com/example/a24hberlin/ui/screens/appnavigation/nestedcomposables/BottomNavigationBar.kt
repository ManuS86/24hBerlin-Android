package com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.ui.screens.appnavigation.AppNavigation
import com.example.a24hberlin.ui.theme.text

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onTitleChange: (String) -> Unit
) {
    val items = listOf(
        NavigationItem(
            route = EVENTS_ROUTE,
            icon = Icons.Filled.Event,
            label = "Events"
        ),
        NavigationItem(
            route = CLUB_MAP_ROUTE,
            icon = Icons.Filled.Map,
            label = "Club Map"
        ),
        NavigationItem(
            route = FAVORITES_ROUTE,
            icon = Icons.Filled.Star,
            label = "Favorites"
        ),
        NavigationItem(
            route = SETTINGS_ROUTE,
            icon = Icons.Filled.Settings,
            label = "Settings"
        )
    )

    NavigationBar(
        containerColor = Color.Black
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    onTitleChange(item.label)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.Gray.copy(0.8f)
                )
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)