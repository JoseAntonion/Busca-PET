package com.example.buscapet.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.buscapet.ui.screens.login.LoginScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    composable<NavDestinations.Login> { LoginScreen(navController = navController) }
}
