package com.example.buscapet.ui.screens.detail_report

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.ui.commons.AppBarWithBack
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun ReportDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    petName: String = "",
    petImage: Int = R.drawable.dummy_puppy,
    petAge: String = "",
    petBreed: String = "",
    petDescription: String = ""
) {
    BuscaPetTheme {
        Scaffold(
            topBar = {
                AppBarWithBack(
                    title = "Detalle de $petName"
                ) { navController.popBackStack() }
            }
        ) {
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(it)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = modifier
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = petImage),
                            contentDescription = "Pet detail image",
                            contentScale = ContentScale.FillHeight,
                            modifier = modifier
                                .fillMaxSize()
                        )
                    }
                    Box(
                        modifier = modifier
                            .weight(2f)
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = petName,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            InfoSection(
                                title = "Edad",
                                content = petAge
                            )
                            InfoSection(
                                title = "Raza",
                                content = petBreed
                            )
                            InfoSection(
                                title = "Descripción",
                                content = petDescription
                            )
                        }
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
            HorizontalDivider(
                modifier = Modifier
                    .padding(0.dp, 14.dp, 0.dp, 0.dp)
            )
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
    ReportDetailScreen(
        petName = "Perro prueba",
        petImage = R.drawable.dummy_puppy,
        petDescription = "Descripción del perro prueba",
        petBreed = "Raza pulenta",
        petAge = "Shikitito"
    )
}