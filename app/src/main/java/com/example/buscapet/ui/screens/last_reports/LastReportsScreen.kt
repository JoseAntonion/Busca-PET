package com.example.buscapet.ui.screens.last_reports

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.AppBar
import com.example.buscapet.ui.screens.commons.MainBottomNav
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LastReportsScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName

    Scaffold(
        bottomBar = { MainBottomNav(navController = navController) }, // NavBar
        topBar = { AppBar(userName) } // TOOLBAR
    ) { padding ->
        MainView(
            scaffoldPadding = padding
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainView() {
    Scaffold(
        bottomBar = { MainBottomNav(navController = rememberNavController()) }, // NavBar
        topBar = { AppBar("test profile") } // TOOLBAR
    ) { padding ->
        MainView(
            scaffoldPadding = padding
        )
    }
}

@Composable
fun MainView(
    scaffoldPadding: PaddingValues
) {
    BuscaPetTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(scaffoldPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Last Reports", textAlign = TextAlign.Center)
        }
    }
}