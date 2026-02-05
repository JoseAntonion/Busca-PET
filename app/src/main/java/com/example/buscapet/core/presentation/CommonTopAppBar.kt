package com.example.buscapet.core.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    userName: String,
    onAccountClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    photo: Uri? = null,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                text = "Hola, $userName",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            IconButton(onClick = { onAccountClick() }) {
                val modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
                    .clip(CircleShape)
                if (photo != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photo)
                            .build(),
                        contentDescription = "some",
                        modifier = modifier
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.NoAccounts,
                        contentDescription = null,
                        modifier = modifier,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        navigationIcon = {
            AppBarAction(
                imageVector = Icons.Default.Menu,
                customColor = MaterialTheme.colorScheme.primary
            ) {
                onMenuClick()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBack(
    title: String = stringResource(id = R.string.app_name),
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        navigationIcon = {
            AppBarAction(Icons.AutoMirrored.Filled.ArrowBack) { onBackClick() }
        },
        actions = {
            AppBarAction(
                imageVector = Icons.Default.Search,
                customColor = Color.Transparent
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}

@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    customColor: Color = LocalContentColor.current,
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = customColor
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewWithBack() {
    BuscaPetTheme {
        AppBarWithBack(
            title = "titulo prueba",
            onBackClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewWithoutBack() {
    BuscaPetTheme {
        CommonTopAppBar(userName = "Usuario Prueba")
    }
}