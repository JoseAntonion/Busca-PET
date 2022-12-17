package com.example.buscapet.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.navigation.NavItem
import com.example.buscapet.ui.screens.last_reports.LastReportsScreen
import com.example.buscapet.ui.screens.my_reports.MyReportsScreen

@Composable
fun HomeNavigation(navController: NavHostController) {
    //val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavItem.LastReportNavItem.route
    ) {
        composable(NavItem.LastReportNavItem.screenRoute) {
            LastReportsScreen(navController)
        }
        composable(NavItem.MyReportsNavItem.screenRoute) {
            MyReportsScreen(navController)
        }
    }
}