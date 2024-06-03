package com.example.buscapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.buscapet.ui.screens.detail_report.ReportDetailScreen
import com.example.buscapet.ui.screens.home.HomeScreen
import com.example.buscapet.ui.screens.login.LoginScreen

@Composable
fun RootNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> { LoginScreen(navController = navController) }
        composable<Home> { HomeScreen(navController = navController) }
        composable<DetailReport> { backStackEntry ->
            val pet = backStackEntry.toRoute<DetailReport>()
            ReportDetailScreen(
                navController = navController,
                petName = pet.nombre
            )
        }
    }
}