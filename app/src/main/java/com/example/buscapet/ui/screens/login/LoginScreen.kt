package com.example.buscapet.ui.screens.login

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.buscapet.R
import com.example.buscapet.ui.navigation.Screens
import com.example.buscapet.ui.screens.commons.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

@Composable
fun LoginScreen(
    navController: NavController
) {
    val viewModel: LoginViewModel = viewModel()
    val activity = LocalContext.current as Activity
    val bussy = viewModel.progress.observeAsState(false)
    val activityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.finishLogin(task, navController)
            Log.d("TAG", "LoginScreen:  viewModel.finishLogin")
        }

    MainLoginContainer(
        signInProgress = bussy.value,
        signInButton = {
            navController.navigate(Screens.Home.route)
            //viewModel.loginWithGoogle(activity) {
            //    activityResult.launch(it)
            //}
        },
        vModel = viewModel
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainLoginContainer() {
    MainLoginContainer(
        signInProgress = false,
        {},
        viewModel()
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainLoginContainer(
    signInProgress: Boolean,
    signInButton: () -> Unit,
    vModel: LoginViewModel
) {
    val signInStatus = vModel.signInName.observeAsState("test")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        if (signInProgress) {
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BuscaPet",
                fontSize = 24.sp,
                style = MaterialTheme.typography.h6
            )
            SignInButton(
                text = "Ingresar con Google",
                loadingText = "Ingresando...",
                isLoading = signInProgress,
                icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                onClick = {
                    signInButton.invoke()
                }
            )
        }
    }
}