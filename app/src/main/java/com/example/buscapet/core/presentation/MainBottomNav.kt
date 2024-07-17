package com.example.buscapet.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.core.navigation.BottomNavScreens
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun MainBottomNav(
    navController: NavController = rememberNavController(),
    items: List<BottomNavScreens>
) {
    BuscaPetTheme {
        BottomAppBar(
            cutoutShape = CircleShape,
            backgroundColor = Color.Green
        ) {
            BottomNav(
                navController = navController,
                items = items
            )
        }
    }
}

@Composable
fun BottomNav(
    navController: NavController,
    items: List<BottomNavScreens>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BuscaPetTheme {
        val bottomBarState = items.any { it.route == currentDestination?.route }
        if (bottomBarState) {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.surface,
                //contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        alwaysShowLabel = true,
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewMainBottomNav2() {
    val itemsDummy = mutableListOf(
        BottomNavScreens.MyReports,
        BottomNavScreens.LastReports
    )
    MainBottomNav(items = itemsDummy)
}