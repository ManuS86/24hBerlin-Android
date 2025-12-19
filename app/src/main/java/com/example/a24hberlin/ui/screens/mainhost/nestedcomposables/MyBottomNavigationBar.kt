package com.example.a24hberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a24hberlin.navigation.navItemsData

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    onTabSelected: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val items = navItemsData

    NavigationBar(
        modifier = Modifier.height(108.dp),
        containerColor = Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
            val label = stringResource(id = item.labelResId)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        haptic.performHapticFeedback(TextHandleMove)
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
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = label
                    )
                },
                label = { Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.offset(y = (-8).dp)
                ) },
                alwaysShowLabel = true,
                colors = colors(
                    selectedIconColor = White,
                    selectedTextColor = White,
                    unselectedIconColor = Gray,
                    unselectedTextColor = Gray,
                    indicatorColor = Transparent
                )
            )
        }
    }
}