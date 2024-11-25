package com.example.buscapet.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    userName: String? = "noUser",
    onIconClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = "Hola, ${userName ?: "noUser"}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(onClick = { onIconClick() }) {
                val modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
                    .clip(CircleShape)
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        navigationIcon = {
            AppBarAction(Icons.Default.Menu) {
                onMenuClick()
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewWithoutBack() {
    CommonTopAppBar(userName = "Usuario Prueba")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBack(
    title: String = stringResource(id = R.string.app_name),
    onBackClick: () -> Unit
) {
    TopAppBar(
//        backgroundColor = MaterialTheme.colorScheme.primary,
//        contentColor = MaterialTheme.colorScheme.onPrimary,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            AppBarAction(Icons.Default.ArrowBack) { onBackClick() }// Agregar Boton de accion a la izquierda
        },
        actions = {
            //AppBarAction(Icons.Default.Search) { /*TODO*/ }// Agregar Boton de accion a la derecha
            //AppBarAction(Icons.Default.Share) { /*TODO*/ }// Agregar Boton de accion a la derecha
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewWithBack() {
    AppBarWithBack(
        title = "titulo prueba",
        onBackClick = {}
    )
}


/*@Composable
fun CollapsingToolbar(state: CollapsingToolbarScaffoldState) {
    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .height(150.dp)
        //.pin()
    )

    Text(
        text = "Title",
        modifier = Modifier
            //.road(Alignment.CenterStart, Alignment.BottomEnd)
            .padding(60.dp, 16.dp, 16.dp, 16.dp),
        color = Color.White,
        fontSize = textSize
    )

    Image(
        painter = painterResource(id = R.drawable.ic_arrow_back),
        contentDescription = null,
        modifier = Modifier
            .padding(16.dp)
        //.pin()
    )
}*/

@Composable
private fun AppBarAction(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,// Agregar Boton de accion a la izquierda
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}