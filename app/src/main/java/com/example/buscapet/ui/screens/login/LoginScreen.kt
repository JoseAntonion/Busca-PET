package com.example.buscapet.ui.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
    val inProgress = viewModel.progress.observeAsState(false)
    val activityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.finishLogin(task, signedId)
        }

    MainLoginContainer(
        inProgress = inProgress.value,
        signInButton = {
            viewModel.loginWithGoogle(activity) {
                activityResult.launch(it)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMainLoginContainer() {
    MainLoginContainer(
        inProgress = false
    ) {}
}

@Composable
fun MainLoginContainer(
    inProgress: Boolean,
    signInButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        if (inProgress) {
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BuscaPET"
            )
            Button(onClick = { signInButton.invoke() }) {
                Text(text = "Sign in with Google")
            }
        }
    }
}