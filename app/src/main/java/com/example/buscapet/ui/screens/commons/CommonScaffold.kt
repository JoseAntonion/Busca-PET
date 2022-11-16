package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonScaffoldM2(
    navController: NavController,
    userName: String? = "No Name",
    content: @Composable (PaddingValues) -> Unit
) {
    BuscaPetTheme {
        Scaffold(
            bottomBar = {
                CommonBottomAppBarM2(
                    navController = navController
                )
            },
            floatingActionButton = { CommonFabM2() },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            topBar = { AppBar(userName) } // TOOLBAR
        ) { padding ->
            content(padding)
        }
    }
}

@Composable
fun CommonScaffoldM3(
    navController: NavController,
    userName: String? = "No Name",
    content: @Composable (PaddingValues) -> Unit
) {
    BuscaPetTheme {
        Scaffold(
            bottomBar = {
                CommonBottomAppBarM3(
                    navController = navController
                )
            },
            floatingActionButton = { CommonFabM3() },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            topBar = { AppBar(userName) } // TOOLBAR
        ) { padding ->
            content(padding)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun M2() {
    CommonScaffoldM2(
        navController = rememberNavController()
    ) {

    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun M3() {
    CommonScaffoldM3(
        navController = rememberNavController()
    ) {

    }
}
