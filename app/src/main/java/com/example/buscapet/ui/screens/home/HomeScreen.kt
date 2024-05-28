package com.example.buscapet.ui.screens.home

import android.net.Uri
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.commons.MainBottomNav
import com.example.buscapet.ui.navigation.BottomNavScreens
import com.example.buscapet.ui.navigation.HomeNavGraph
import com.example.buscapet.ui.ui.commons.CommonFloatingActionButton
import com.example.buscapet.ui.ui.commons.CommonTopAppBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val name = currentUser?.displayName?.split(" ")?.get(0)
    val photo = currentUser?.photoUrl

    HomeContainer(
        currentUserName = name,
        profilePhoto = photo
    )
}

@Composable
fun HomeContainer(
    currentUserName: String?,
    profilePhoto: Uri? = null
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { CommonTopAppBar(currentUserName, profilePhoto) },
        bottomBar = {
            val bottomBarItems = listOf(
                BottomNavScreens.MyReports,
                BottomNavScreens.LastReports,
            )
            MainBottomNav(
                navController = navController,
                items = bottomBarItems
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton()
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
    ) { paddingValues ->
        HomeNavGraph(
            navController = navController,
            hostPadding = paddingValues
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    HomeContainer(
        "Padrito"
    )
}