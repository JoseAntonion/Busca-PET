package com.example.buscapet.ui.screens.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.ui.commons.CommonTabBar
import com.example.buscapet.ui.commons.TabItem
import com.example.buscapet.ui.screens.last_reports.LastReportsScreen
import com.example.buscapet.ui.ui.commons.CommonFloatingActionButton
import com.example.buscapet.ui.commons.CommonTopAppBar
import com.example.buscapet.ui.ui.my_reports.MyReportsScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val name = currentUser?.displayName?.split(" ")?.get(0)
    val photo = currentUser?.photoUrl
    val tabItems = listOf(
        TabItem(
            title = "Ultimos reportes",
            icon = Icons.Default.Pets
        ),
        TabItem(
            title = "Mis reportes",
            icon = ImageVector.vectorResource(id = R.drawable.ic_dog)
        )
    )

    HomeContainer(
        navController = navController,
        currentUserName = name,
        profilePhoto = photo,
        tabItems = tabItems
    )
}

@Composable
fun HomeContainer(
    navController: NavController = rememberNavController(),
    currentUserName: String?,
    profilePhoto: Uri? = null,
    tabItems: List<TabItem> = listOf()
) {
    val pagerState = rememberPagerState(0, 0f) { tabItems.size }
    val scope = rememberCoroutineScope()
    val currentScreen = navController.currentBackStackEntry?.destination?.route
    Log.d("TAG", "HomeContainer: currentScreen $currentScreen")
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommonTopAppBar(currentUserName, profilePhoto)
                CommonTabBar(
                    pagerState = pagerState,
                    scope = scope,
                    tabItems = tabItems
                )
            }
        },
        floatingActionButton = {
            CommonFloatingActionButton()
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) { index ->
            when (index) {
                0 -> LastReportsScreen(navController = navController)
                1 -> MyReportsScreen()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    HomeContainer(currentUserName = "John Johnson")
}