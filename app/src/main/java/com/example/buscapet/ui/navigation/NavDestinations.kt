package com.example.buscapet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import kotlinx.serialization.Serializable

sealed class NavDestinations {

    @Serializable
    object Login

    @Serializable
    object Home

    @Serializable
    object ScreenContent

    @Serializable
    object LastReports {
        val route = "last_reports"
        val title = "Últimos Reportes"
        val icon = Icons.Default.Home
    }

    @Serializable
    object MyReports {
        val route = "my_reports"
        val title = "Mis Reportes"
        val icon = Icons.Default.AccountCircle
    }
}