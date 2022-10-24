package com.example.buscapet.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.navigation.NavArgs
import com.example.buscapet.ui.navigation.NavItem
import com.example.buscapet.ui.screens.home.HomeScreen
import com.example.buscapet.ui.screens.login.LoginScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavItem.LoginNavItem.route
    ) {
        composable(NavItem.LoginNavItem) {
            LoginScreen {
                navController.navigate(NavItem.HomeNavItem.route)
            }
            //MainScreen(
            //    navController = navController,
            //    viewModel = viewModel()
            //) {
            //    val route = NavItem.DetalleNavItem.createNavRoute(it)
            //    navController.navigate(route)
            //}
        }
        composable(NavItem.HomeNavItem) {
            HomeScreen(
                navController = navController
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