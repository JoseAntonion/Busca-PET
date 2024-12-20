package com.example.buscapet.profile.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.buscapet.core.presentation.AppBarWithBack

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // init values
    val uiState by viewModel.uiState.collectAsState()

    // Main Container
    MainContainer(
        navController = navController,
        userName = uiState.name,
        userEmail = uiState.email,
        userImage = uiState.photo
    )
}

@Composable
fun MainContainer(
    navController: NavController,
    userName: String? = "noUser",
    userEmail: String? = "noEmail",
    userImage: Uri? = null,
) {
    Scaffold(
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
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
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

                    if (userImage != null)
                        AsyncImage(
                            model = userImage,
                            contentDescription = stringResource(id = R.string.profile_user_image_content_description),
                            contentScale = ContentScale.FillBounds,
                            modifier = imageModifier,
                        )
                    else
                        Icon(
                            modifier = imageModifier,
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = stringResource(id = R.string.profile_user_image_content_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                }
                Box {
                    Column {
                        InfoSection(
                            title = stringResource(id = R.string.profile_user_name),
                            content = userName!!
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(0.dp, 14.dp, 0.dp, 0.dp)
                        )
                        InfoSection(
                            title = stringResource(id = R.string.profile_user_email),
                            content = userEmail!!
                        )
                    }
                }
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
    Box {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = modifier
                    .padding(0.dp, 14.dp, 0.dp, 0.dp),
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                modifier = modifier
                    .padding(0.dp, 8.dp, 0.dp, 0.dp),
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
    MainContainer(navController = rememberNavController())
}