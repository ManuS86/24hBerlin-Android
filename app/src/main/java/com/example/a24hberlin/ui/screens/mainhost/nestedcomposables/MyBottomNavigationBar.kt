package com.example.a24hberlin.ui.screens.mainhost.nestedcomposables

import android.view.SoundEffectConstants
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a24hberlin.navigation.navItemsData

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    onTabSelected: () -> Unit
) {
    val view = LocalView.current
    val items = navItemsData

    NavigationBar(
        containerColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route

            val label = stringResource(id = item.labelResId)

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = label
                    )
                },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onTabSelected()
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}