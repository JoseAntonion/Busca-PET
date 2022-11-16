package com.example.buscapet.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    val context = LocalContext.current
    MainView(userName, context, navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainView(
    userName: String?,
    context: Context,
    navController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    BuscaPetTheme {
//        Scaffold(
//            scaffoldState = scaffoldState,
//            bottomBar = { MainBottomNav(navController = navController) }, // NavBar
//            floatingActionButton = { FabCommon(context = context) },
//            isFloatingActionButtonDocked = true,
//            floatingActionButtonPosition = FabPosition.Center,
//            topBar = { AppBar(userName) } // TOOLBAR
//        ){
//
//        }
        //            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(color = MaterialTheme.colorScheme.primary)
//                    .padding(padding),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Mis Reportes",
//                    textAlign = TextAlign.Center,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
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