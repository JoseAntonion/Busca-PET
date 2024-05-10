package com.example.buscapet.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.buscapet.R

sealed class NavItem(
    var title: String,
    var icon: Int,
    var screenRoute: String,
    val navArgs: List<NavArgs> = emptyList()
) {
    val route = run {
        // baseroute/{arg1}/{arg2}
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(screenRoute)
            .plus(argKeys)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

    data object HomeNavItem : NavItem(
        title = "Home",
        icon = R.drawable.ic_dog_paw,
        screenRoute = "home"
    )

    data object LastReportNavItem : NavItem(
        title = "Últimos Reportes",
        icon = R.drawable.ic_dog_paw,
        screenRoute = "last_reports"
    )

    data object MyReportsNavItem : NavItem(
        title = "Mis Reportes",
        icon = R.drawable.ic_dog_paw,
        screenRoute = "my_reports"
    )

    data object LoginNavItem : NavItem(
        title = "Login",
        icon = R.drawable.ic_dog_paw,
        screenRoute = "login"
    )

    data object DetalleNavItem : NavItem(
        title = "detalle titulo",
        icon = R.drawable.ic_dog_paw,
        screenRoute = "detalle",
        navArgs = listOf(NavArgs.TopicoId)
    ) {
        fun createNavRoute(topicoId: String) = "$screenRoute/$topicoId"
    }
}

enum class NavArgs(val key: String, val navType: NavType<*>) {
    TopicoId("topicoId", NavType.StringType),
    TopicoName("topicoName", NavType.StringType),
}