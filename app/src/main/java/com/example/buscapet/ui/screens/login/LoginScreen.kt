package com.example.buscapet.ui.screens.login

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buscapet.R
import com.example.buscapet.ui.screens.commons.SignInButton
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

@Composable
fun LoginScreen(signedId: () -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val activity = LocalContext.current as Activity
    val bussy = viewModel.progress.observeAsState(false)
    val activityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.finishLogin(task, signedId)
        }

    MainLoginContainer(
        signInProgress = bussy.value,
        signInButton = {
            viewModel.loginWithGoogle(activity) {
                activityResult.launch(it)
            }
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainLoginContainer() {
    MainLoginContainer(
        signInProgress = false
    ) {}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainLoginContainer(
    signInProgress: Boolean,
    signInButton: () -> Unit
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
                SignInButton(
                    text = stringResource(R.string.login_google_sing_in_button_text),
                    loadingText = stringResource(R.string.login_google_sing_in_button_loading_text),
                    isLoading = signInProgress,
                    icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                    onClick = {
                        signInButton.invoke()
                    }
                )
            }
        }
    }
}