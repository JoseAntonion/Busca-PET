package com.example.buscapet.ui.screens.my_reports

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.AppBar
import com.example.buscapet.ui.screens.commons.FabCommon
import com.example.buscapet.ui.screens.commons.MainBottomNav
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyReportsScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    val context = LocalContext.current
    MainView(userName, context, navController)
}

@Composable
fun MainView(
    userName: String?,
    context: Context,
    navController: NavHostController
) {
    BuscaPetTheme {
        Scaffold(
            bottomBar = { MainBottomNav(navController = navController) }, // NavBar
            floatingActionButton = { FabCommon(context = context) },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            topBar = { AppBar(userName) } // TOOLBAR
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mis Reportes",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainView() {
    MainView(
        userName = "Test",
        context = LocalContext.current,
        navController = rememberNavController()
    )
}