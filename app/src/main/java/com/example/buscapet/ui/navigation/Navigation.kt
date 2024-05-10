package com.example.buscapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.home.HomeScreen
import com.example.buscapet.ui.screens.last_reports.LastReportsScreen
import com.example.buscapet.ui.screens.login.LoginScreen
import com.example.buscapet.ui.screens.my_reports.MyReportsScreen

@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavItem.LoginNavItem.route
    ) {
        composable(NavItem.LoginNavItem) {
            LoginScreen {
                navHostController.navigate(NavItem.LastReportNavItem.route)
            }
        }
        composable(NavItem.LastReportNavItem) {
            LastReportsScreen(
                navController = navHostController
            )
        }
        composable(NavItem.MyReportsNavItem) {
            MyReportsScreen(
                navController = navHostController
            )
        }
        composable(NavItem.HomeNavItem) {
            HomeScreen(
                navController = navHostController
            )
        }
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArgs): T {
    val value = arguments?.get(arg.key)
    requireNotNull(value)
    return value as T
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}