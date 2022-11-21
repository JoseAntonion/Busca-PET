package com.example.buscapet.ui.screens.my_reports

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.CommonAlertDialog
import com.example.buscapet.ui.screens.commons.CommonScaffoldM3
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyReportsScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    val activity = (LocalContext.current as? Activity)
    BackHandler { activity?.finishAffinity() }
    MainView(userName, navController)
}

@Composable
fun MainView(
    userName: String?,
    navController: NavHostController
) {
    val viewModel: MyReportsViewModel = viewModel()
    val showDialog = viewModel.showDialog.observeAsState(false)

    BuscaPetTheme {
        CommonScaffoldM3(
            navController = navController,
            userName = userName,
            content = { padding ->
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
            },
            fabAction = {
                viewModel.dialogState(true)
            }
        )
    }

    if (showDialog.value) {
        CommonAlertDialog {
            viewModel.dialogState(false)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainView() {
    MainView(
        userName = "Test",
        navController = rememberNavController()
    )
}