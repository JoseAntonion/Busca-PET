package com.example.buscapet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreens(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Login : BottomNavScreens(
        route = "login",
        title = "Login",
        icon = Icons.Default.Info
    )

    data object Home : BottomNavScreens(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object LastReports : BottomNavScreens(
        route = "last-reports",
        title = "Últimos reportes",
        icon = Icons.Default.LocationOn
    )

    data object MyReports : BottomNavScreens(
        route = "my_reports",
        title = "Mis reportes",
        icon = Icons.Default.Person
    )
}