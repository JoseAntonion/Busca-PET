package com.example.buscapet.ui.screens.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
            Log.d("TAG", "LoginScreen:  viewModel.finishLogin")
        }

    MainLoginContainer(
        signInProgress = bussy.value,
        signInButton = {
            viewModel.loginWithGoogle(activity) {
                activityResult.launch(it)
            }
        },
        vModel = viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMainLoginContainer() {
    MainLoginContainer(
        signInProgress = false,
        {},
        viewModel()
    )
}

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
                text = "BuscaPET",
                fontSize = 24.sp,
                style = MaterialTheme.typography.h6
            )
            Button(
                onClick = { signInButton() },
                enabled = !signInProgress
            ) {
                Text(
                    text = "Ingresar con Google", color = MaterialTheme.colors.background
                )
            }
        }
    }
}