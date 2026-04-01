package com.example.buscapet.auth.presentation

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.buscapet.R
import com.example.buscapet.core.navigation.Home
import com.example.buscapet.core.presentation.CommonLoadingOverlay
import com.example.buscapet.core.presentation.GoogleButtonTheme
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
            try {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(intent.data)
                viewModel.onEvent(SignInEvent.SignInWithData(task))
            } catch (e: Exception) {
                viewModel.onEvent(SignInEvent.SignInWithData(null))
            }
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

    Box(modifier = Modifier.fillMaxSize()) {
        MainLoginContainer(
            uiState = uiState,
            signInButton = { viewModel.onEvent(SignInEvent.SignInButtonPressed(activity)) }
        )
        
        CommonLoadingOverlay(
            isLoading = uiState.loading || uiState.frontLoading,
            message = "Ingresando..."
        )
    }
}

@Composable
fun MainLoginContainer(
    uiState: SignInState,
    signInButton: () -> Unit
) {
    // Gradiente tenue (pasteles suaves) similar a los tonos del logo
    val softGradientBackground = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD1C4E9), // Púrpura suave
            Color(0xFFB2EBF2), // Cyan suave
            Color(0xFFF0F4C3)  // Lima/Amarillo suave
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = softGradientBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.6f))
        
        Image(
            painter = painterResource(id = R.drawable.vector_buscapet),
            contentDescription = "BuscaPet Logo",
            // Ya no se requiere una dimensión específica, usa el espacio original de la imagen
            modifier = Modifier
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        SignInButton(
            text = "Ingresar con Google",
            loadingText = "Ingresando...",
            isLoading = false,
            // Como el fondo es claro y tenue, la variante oscura del botón resalta perfectamente
            theme = GoogleButtonTheme.Dark,
            icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
            onClick = { signInButton() }
        )
        
        Spacer(modifier = Modifier.height(100.dp))
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