package com.example.a24hberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a24hberlin.R
import com.example.a24hberlin.navigation.Screen

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    onTitleChange: (String) -> Unit
) {
    val items = listOf(
        NavigationItem(
            route = Screen.Events.route,
            selectedIcon = Icons.Filled.Event,
            unselectedIcon = Icons.Outlined.Event,
            label = stringResource(R.string.events)
        ),
        NavigationItem(
            route = Screen.ClubMap.route,
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map,
            label = stringResource(R.string.club_map)
        ),
        NavigationItem(
            route = Screen.Favorites.route,
            selectedIcon = Icons.Rounded.Star,
            unselectedIcon = Icons.Rounded.StarOutline,
            label = stringResource(R.string.my_events)
        ),
        NavigationItem(
            route = Screen.Settings.route,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            label = stringResource(R.string.settings)
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
                    unselectedIconColor = Color.Gray.copy(0.9f)
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