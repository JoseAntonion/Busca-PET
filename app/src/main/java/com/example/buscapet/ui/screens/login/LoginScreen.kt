package com.example.buscapet.ui.screens.login

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.buscapet.R
import com.example.buscapet.ui.screens.commons.SignInButton
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    // TODO: Resoler redireccion cuando encuentra session al iniciar la app

    val viewModel: LoginViewModel = viewModel()
    val activity = LocalContext.current as Activity
    val logginIn = viewModel.logginIn.observeAsState(false)
    val checkingSession = viewModel.checkingSession.observeAsState(false)
    val activityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.finishLogin(task, navController)
        }
    MainLoginContainer(
        signInProgress = logginIn.value, signInButton = {
            viewModel.loginWithGoogle(activity) {
                activityResult.launch(it)
            }
        }, checkingSession = checkingSession.value
    )

    //viewModel.checkSession(navController)
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainLoginContainer() {
    MainLoginContainer(signInProgress = false, checkingSession = true, signInButton = {})
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainLoginContainer(
    signInProgress: Boolean, checkingSession: Boolean, signInButton: () -> Unit
) {
    BuscaPetTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.login_app_name),
                    color = MaterialTheme.colorScheme.inverseSurface,
                    style = MaterialTheme.typography.titleLarge
                )
//                if (checkingSession) {
//                    Box {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.padding(bottom = 16.dp),
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                            Text(text = "Validando sesión")
//                        }
//                    }
//                }
                //if (!checkingSession) {
                    SignInButton(text = stringResource(R.string.login_google_sing_in_button_text),
                        loadingText = stringResource(R.string.login_google_sing_in_button_loading_text),
                        isLoading = signInProgress,
                        icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                        onClick = {
                            signInButton.invoke()
                        })
                //}
            }
        }
    }
}