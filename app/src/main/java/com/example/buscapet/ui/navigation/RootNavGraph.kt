package com.example.buscapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.home.HomeScreen
import com.example.buscapet.ui.screens.login.LoginScreen
import com.example.buscapet.ui.screens.report_detail.ReportDetailScreen

@Composable
fun RootNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BottomNavScreens.Login.route
    ) {
        composable(BottomNavScreens.Login.route) { LoginScreen(navController = navController) }
        composable(BottomNavScreens.Home.route) { HomeScreen(navController = navController) }
        composable(BottomNavScreens.DetailReport.route) { ReportDetailScreen(navController = navController) }
    }
}