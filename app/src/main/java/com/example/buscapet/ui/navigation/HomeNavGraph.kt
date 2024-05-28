package com.example.buscapet.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.buscapet.ui.screens.last_reports.LastReportsScreen
import com.example.buscapet.ui.ui.my_reports.MyReportsScreen

@Composable
fun HomeNavGraph(navController: NavHostController, hostPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreens.LastReports.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(hostPadding)
    ) {
        composable(BottomNavScreens.LastReports.route) { LastReportsScreen(navController = navController) }
        composable(BottomNavScreens.MyReports.route) { MyReportsScreen(navController = navController) }
    }
}