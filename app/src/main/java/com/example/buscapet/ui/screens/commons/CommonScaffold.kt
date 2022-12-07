package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonScaffoldM2(
    navController: NavController,
    userName: String? = "No Name",
    content: @Composable (PaddingValues) -> Unit,
    fabAction: () -> Unit
) {
    BuscaPetTheme {
        Scaffold(
            bottomBar = {
                CommonBottomAppBarM2(
                    navController = navController
                )
            },
            floatingActionButton = { CommonFabM2(fabAction) },
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
    userName: String? = "No Name",
    content: @Composable (PaddingValues) -> Unit,
    fabAction: @Composable () -> Unit = {},
    topAppBarIcon: ImageVector,
    topAppBarIconClick: () -> Unit,
    bottomAppBar: @Composable () -> Unit = {},
    snackbarHostState: @Composable (androidx.compose.material.SnackbarHostState) -> Unit
) {
    BuscaPetTheme {
        Scaffold(
            bottomBar = { bottomAppBar() },
            floatingActionButton = { fabAction() },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            topBar = {
                //AppBar(userName)
                CommonCenterAlignedTopAppBar(
                    profileName = userName,
                    navIcon = topAppBarIcon,
                    onNavIconClick = { topAppBarIconClick() },
                    action = {}
                )
            }, snackbarHost = snackbarHostState
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
        navController = rememberNavController(),
        fabAction = {},
        content = {}
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun M3() {
    val snackbarHostState = remember { SnackbarHostState() }
    CommonScaffoldM3(
        content = {},
        fabAction = {},
        topAppBarIcon = Icons.Default.Favorite,
        topAppBarIconClick = {},
        userName = "Demo UserName",
        snackbarHostState = {
            SnackbarHost(snackbarHostState)
        }
    )
}
