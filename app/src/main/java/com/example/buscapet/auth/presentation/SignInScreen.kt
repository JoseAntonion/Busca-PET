package com.example.buscapet.auth.presentation

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.buscapet.R
import com.example.buscapet.core.navigation.Home
import com.example.buscapet.core.presentation.SignInButton
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(intent.data)
            viewModel.onEvent(SignInEvent.SignInWithData(task))
        }

    LaunchedEffect(context) {
        viewModel.signInChannel.collect { event ->
            when (event) {
                is SignInEventResult.OnAccountSelectorIntent -> activityResult.launch(event.intent)
                is SignInEventResult.OnSignInSuccess -> navController.navigate(Home)
                is SignInEventResult.OnSignInError -> Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    MainLoginContainer(
        uiState = uiState,
        signInButton = { viewModel.onEvent(SignInEvent.SignInButtonPressed(activity)) }
    )
}

@Composable
fun MainLoginContainer(
    uiState: SignInState,
    signInButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = uiState.frontLoading, enter = fadeIn(), exit = fadeOut()) {
            CircularProgressIndicator()
        }
        AnimatedVisibility(visible = !uiState.frontLoading, enter = fadeIn(), exit = fadeOut()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BuscaPet",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                SignInButton(
                    text = "Ingresar con Google",
                    loadingText = "Ingresando...",
                    borderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    isLoading = uiState.loading,
                    icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                    onClick = { signInButton() }
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun Preview() {
    BuscaPetTheme {
        MainLoginContainer(
            uiState = SignInState(loading = true),
        ) {}
    }

}