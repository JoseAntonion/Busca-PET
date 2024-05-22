package com.example.buscapet.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.navigation.HomeNavGraph
import com.example.buscapet.ui.navigation.Screens
import com.example.buscapet.ui.screens.commons.CommonTopAppBar
import com.example.buscapet.ui.screens.commons.MainBottomNav
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val name = currentUser?.displayName?.split(" ")?.get(0)
    val photo = currentUser?.photoUrl

    HomeContainer(
        navController = navController,
        currentUserName = name,
        profilePhoto = photo
    )
}

@Composable
fun HomeContainer(
    navController: NavHostController,
    currentUserName: String?,
    profilePhoto: Uri? = null
) {

    Scaffold(
        topBar = { CommonTopAppBar(currentUserName, profilePhoto) },
        bottomBar = {
            val bottomBarItems = listOf(
                Screens.MyReports,
                Screens.LastReports,
            )
            MainBottomNav(
                navController = navController,
                items = bottomBarItems
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(90.dp),
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                shape = CircleShape
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(fontSize = 14.sp, text = "Reportar")
                }
            }
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
        rememberNavController(),
        "Padrito"
    )
}