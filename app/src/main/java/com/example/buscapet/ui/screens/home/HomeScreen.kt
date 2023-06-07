package com.example.buscapet.ui.screens.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.CommonBottomAppBarM3
import com.example.buscapet.ui.screens.commons.CommonFabM3
import com.example.buscapet.ui.screens.commons.CommonScaffoldM3
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    MainView(userName, navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainView(
    userName: String?,
    navControllerOLD: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val navController = rememberNavController()
    BuscaPetTheme {
        CommonScaffoldM3(
            userName = userName,
            content = {
                HomeNavigation(navController)
            },
            fabAction = {
                CommonFabM3 { viewModel.dialogState(true) }
            },
            topAppBarIcon = Icons.Default.Menu,
            topAppBarIconClick = {},
            bottomAppBar = {
                CommonBottomAppBarM3(
                    navController = navController
                )
            },
            snackbarHostState = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainView() {
    MainView(
        userName = "Test",
        navControllerOLD = rememberNavController()
    )
}