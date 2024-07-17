package com.example.buscapet.core.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonTopAppBar(
    userName: String?,
    photo: Uri? = null
) {
    BuscaPetTheme {
        TopAppBar(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            title = {
                Text(
                    text = "Hola $userName",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            actions = {
                IconButton(onClick = { /* do something */ }) {
                    if (photo != null) {
                        AsyncImage(
                            model = photo,
                            contentDescription = "Profile photo",
                            modifier = Modifier.clip(CircleShape)
                        )
                    } else {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    }
                }
                //AppBarAction(Icons.Default.Search) { /*TODO*/ }// Agregar Boton de accion a la derecha
                //AppBarAction(Icons.Default.Share) { /*TODO*/ }// Agregar Boton de accion a la derecha
            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun Preview(modifier: Modifier = Modifier) {
    CommonTopAppBar(userName = "Usuario Prueba")
}

@Composable
fun AppBarWithBack(
    title: String = stringResource(id = R.string.app_name),
    onBackClick: () -> Unit
) {
    BuscaPetTheme {
        TopAppBar(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
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
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewWithBack(modifier: Modifier = Modifier) {
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
            contentDescription = null
        )
    }
}