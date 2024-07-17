package com.example.buscapet.auth.presentation.sign_in

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.buscapet.R
import com.example.buscapet.core.presentation.SignInButton
import com.example.buscapet.core.navigation.Home
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
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
            navController.navigate(Home)
            //viewModel.loginWithGoogle(activity) {
            //    activityResult.launch(it)
            //}
        },
        vModel = viewModel
    )
}

@Composable
fun MainLoginContainer(
    signInProgress: Boolean,
    signInButton: () -> Unit,
    vModel: SignInViewModel
) {
    val signInStatus = vModel.signInName.observeAsState("test")

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (signInProgress) {
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BuscaPet",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            SignInButton(
                text = "Ingresar con Google",
                loadingText = "Ingresando...",
                borderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                isLoading = signInProgress,
                icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                onClick = {
                    signInButton()
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun Preview() {
    BuscaPetTheme {
        MainLoginContainer(
            signInProgress = false,
            {},
            viewModel()
        )
    }

}