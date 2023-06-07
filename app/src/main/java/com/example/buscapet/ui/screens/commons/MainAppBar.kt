package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.buscapet.ui.theme.BuscaPetTheme

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppBar() {
    AppBar(profileName = "Prueba")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(profileName: String?) {
    BuscaPetTheme {
        TopAppBar(
            title = {
                Text(
                    text = "Hola $profileName",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            actions = {
                //AppBarAction(Icons.Default.Search) { /*TODO*/ }// Agregar Boton de accion a la derecha
                //AppBarAction(Icons.Default.Share) { /*TODO*/ }// Agregar Boton de accion a la derecha
                AppBarAction(Icons.Default.Menu) { /*TODO*/ }// Agregar Boton de accion a la derecha
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonCenterAlignedTopAppBar(
    profileName: String?,
    navIcon: ImageVector,
    onNavIconClick: () -> Unit = {},
    action: @Composable () -> Unit
) {
    BuscaPetTheme {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Hola $profileName",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { onNavIconClick() }) {
                    Icon(
                        imageVector = navIcon,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = { action() }
        )
    }
}

@Preview
@Composable
fun PreviewCommonCenterAlignedTopAppBar() {
    CommonCenterAlignedTopAppBar(
        profileName = "Demo User",
        navIcon = Icons.Default.Menu,
        onNavIconClick = {},
        action = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Preview
@Composable
fun Preview2CommonCenterAlignedTopAppBar() {
    CommonCenterAlignedTopAppBar(
        profileName = "Demo User",
        navIcon = Icons.Default.ArrowBack,
        onNavIconClick = {},
        action = {}
    )
}

@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,// Agregar Boton de accion a la izquierda
            contentDescription = null
        )
    }
}