package com.example.buscapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.last_reports.LastReportsScreen
import com.example.buscapet.ui.screens.report_detail.ReportDetailScreen

@Composable
fun DetailNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BottomNavScreens.LastReports.route
    ) {
        composable(BottomNavScreens.LastReports.route) { LastReportsScreen(navController) }
        composable(BottomNavScreens.DetailReport.route) { ReportDetailScreen() }
    }
}