package com.example.buscapet.profile.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.buscapet.R
import com.example.buscapet.core.navigation.SignIn
import com.example.buscapet.core.presentation.AppBarWithBack
import com.example.buscapet.core.presentation.CommonLoadingOverlay
import com.example.buscapet.core.presentation.CommonLongButton
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggingOut by viewModel.isLoggingOut.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collect {
            navController.navigate(SignIn) {
                popUpTo(0)
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MainContainer(
            navController = navController,
            userName = uiState.name,
            userEmail = uiState.email,
            userImage = uiState.photo,
            onLogoutClick = { showLogoutDialog = true }
        )
        CommonLoadingOverlay(
            isLoading = isLoggingOut,
            message = "Cerrando sesión..."
        )
    }
}

@Composable
fun MainContainer(
    navController: NavController,
    userName: String? = "noUser",
    userEmail: String? = "noEmail",
    userImage: Uri? = null,
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        topBar = {
            AppBarWithBack(
                title = stringResource(id = R.string.profile_topbar_title),
                onBackClick = { navController.navigateUp() }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val imageModifier = Modifier
                        .height(220.dp)
                        .width(220.dp)
                        .clip(CircleShape)

                    if (userImage != null) {
                        val highResUrl = userImage.toString().replace("s96-c", "s400-c")
                        AsyncImage(
                            model = highResUrl,
                            contentDescription = stringResource(id = R.string.profile_user_image_content_description),
                            contentScale = ContentScale.Crop,
                            modifier = imageModifier,
                        )
                    } else
                        Icon(
                            modifier = imageModifier,
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(id = R.string.profile_user_image_content_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                }
                Card {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                    ) {
                        InfoSection(
                            title = stringResource(id = R.string.profile_user_name),
                            content = userName ?: "Sin Nombre"
                        )
                        InfoSection(
                            title = stringResource(id = R.string.profile_user_email),
                            content = userEmail ?: "Sin Email"
                        )
                    }
                }
                CommonLongButton(
                    text = "Cerrar sesión",
                    textColor = MaterialTheme.colorScheme.onErrorContainer,
                    customButtonColors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    title: String = "",
    content: String = ""
) {
    Box(
        modifier = Modifier.padding(14.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
private fun Preview() {
    BuscaPetTheme {
        MainContainer(navController = rememberNavController())
    }
}