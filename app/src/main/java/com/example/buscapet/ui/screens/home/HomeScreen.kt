package com.example.buscapet.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.AppBar
import com.example.buscapet.ui.screens.commons.MainBottomNav
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName

    Scaffold(
        bottomBar = { MainBottomNav(navController = navController) }, // NavBar
        topBar = { AppBar() } // TOOLBAR
    ) { padding ->
        MainView(
            scaffoldPadding = padding,
            sessionName = userName
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    Scaffold(
        bottomBar = { MainBottomNav(navController = rememberNavController()) }, // NavBar
        topBar = { AppBar() } // TOOLBAR
    ) { padding ->
        MainView(
            scaffoldPadding = padding,
            sessionName = "Prueba"
        )
    }
}

@Composable
fun MainView(
    scaffoldPadding: PaddingValues,
    sessionName: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(scaffoldPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Welcome $sessionName", textAlign = TextAlign.Center)
    }
}