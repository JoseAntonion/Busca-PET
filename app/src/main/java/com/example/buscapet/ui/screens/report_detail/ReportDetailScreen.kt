package com.example.buscapet.ui.screens.report_detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun ReportDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    BuscaPetTheme {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Detalle de la mascota",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
private fun Preview() {
    ReportDetailScreen()
}