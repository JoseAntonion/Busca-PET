package com.example.buscapet.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.navigation.NavItem
import com.example.buscapet.ui.navigation.Navigation
import com.example.buscapet.ui.screens.commons.AppBar
import com.example.buscapet.ui.screens.commons.CommonBottomNavigation
import com.example.buscapet.ui.screens.commons.CommonFabM3
import com.example.buscapet.ui.theme.BuscaPetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuscaPetTheme {
                InitActivity()
            }
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InitActivity() {
    val navController = rememberNavController()
    val bottomNavScreens = listOf(
        NavItem.LastReportNavItem,
        NavItem.MyReportsNavItem
    )
    val showBottomBar =
        navController.currentBackStackEntryAsState().value?.destination?.route in bottomNavScreens.map { it.route }
    Scaffold(
        bottomBar = {
            if (showBottomBar) CommonBottomNavigation(
                navController = navController
            )
        }, // NavBar
        floatingActionButton = { if (showBottomBar) CommonFabM3() },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = { if (showBottomBar) AppBar("PROANDING") } // TOOLBAR
    ) {
        Navigation()
    }
}