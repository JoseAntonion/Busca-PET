package com.example.buscapet.ui.screens.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.buscapet.R

@Preview
@Composable
fun PreviewAppBar() {
    AppBar(profileName = "Prueba")
}

@Composable
fun AppBar(profileName: String?) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        //contentColor = MaterialTheme.colors.onSecondary,
        title = {
            Text(
                text = "Hola $profileName",
                style = MaterialTheme.typography.h5,
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

@Composable
fun AppBarWithBack(
    title: String = stringResource(id = R.string.app_name),
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    onBackClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
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

//@Composable
//fun CollapsingToolbar(state: CollapsingToolbarScaffoldState) {
//    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
//
//    Box(
//        modifier = Modifier
//            .background(MaterialTheme.colors.primary)
//            .fillMaxWidth()
//            .height(150.dp)
//        //.pin()
//    )
//
//    Text(
//        text = "Title",
//        modifier = Modifier
//            //.road(Alignment.CenterStart, Alignment.BottomEnd)
//            .padding(60.dp, 16.dp, 16.dp, 16.dp),
//        color = Color.White,
//        fontSize = textSize
//    )
//
//    Image(
//        painter = painterResource(id = R.drawable.ic_arrow_back),
//        contentDescription = null,
//        modifier = Modifier
//            .padding(16.dp)
//        //.pin()
//    )
//}

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