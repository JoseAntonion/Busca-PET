package com.example.buscapet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Login : Screens(
        route = "login",
        title = "Login",
        icon = Icons.Default.Info
    )

    data object Home : Screens(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object LastReports : Screens(
        route = "last-reports",
        title = "Últimos reportes",
        icon = Icons.Default.LocationOn
    )

    data object MyReports : Screens(
        route = "my-reports",
        title = "Mis reportes",
        icon = Icons.Default.Person
    )
}