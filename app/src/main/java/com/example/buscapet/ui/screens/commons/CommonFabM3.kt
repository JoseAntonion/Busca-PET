package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonFabM3() {
    BuscaPetTheme {
        FloatingActionButton(
            onClick = { /*TODO*/ },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_report),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun CommonFabM2() {
    BuscaPetTheme {
        androidx.compose.material.FloatingActionButton(
            onClick = { /*TODO*/ },
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_report),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCommonFabM3() {
    CommonFabM3()
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCommonFabM2() {
    CommonFabM2()
}