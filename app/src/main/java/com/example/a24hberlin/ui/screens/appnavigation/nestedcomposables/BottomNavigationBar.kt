package com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

const val EVENTS_ROUTE = "events"
const val CLUB_MAP_ROUTE = "club_map"
const val FAVORITES_ROUTE = "favorites"
const val SETTINGS_ROUTE = "settings"

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onTitleChange: (String) -> Unit
) {
    val items = listOf(
        NavigationItem(
            route = EVENTS_ROUTE,
            selectedIcon = Icons.Filled.Event,
            unselectedIcon = Icons.Outlined.Event,
            label = "Events"
        ),
        NavigationItem(
            route = CLUB_MAP_ROUTE,
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map,
            label = "Club Map"
        ),
        NavigationItem(
            route = FAVORITES_ROUTE,
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.StarOutline,
            label = "Favorites"
        ),
        NavigationItem(
            route = SETTINGS_ROUTE,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            label = "Settings"
        )
    )

    NavigationBar(
        containerColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            val selected = currentDestination?.route == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                selected = selected,
                onClick = {
                    onTitleChange(item.label)
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
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
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String
)