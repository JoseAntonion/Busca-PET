package com.example.buscapet.detail_report.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.presentation.AppBarWithBack

@Composable
fun DetailReportScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailReportViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    petId: Int,
) {
    viewModel.setPetId(petId)
    val petDetail by viewModel.petDetails.collectAsState()
    MainContainer(
        navController = navController,
        petDetail = petDetail
    )
}

@Composable
fun MainContainer(
    navController: NavController = rememberNavController(),
    petDetail: Pet? = null
) {
    Scaffold(
        topBar = {
            AppBarWithBack(
                title = "Detalle de ${petDetail?.name}"
            ) { navController.navigateUp() }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_puppy),
                        contentDescription = "Pet detail image",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(2f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = petDetail?.name ?: "no name",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        InfoSection(
                            title = "Edad",
                            content = petDetail?.age ?: "no age"
                        )
                        InfoSection(
                            title = "Raza",
                            content = petDetail?.breed ?: "no breed"
                        )
                        InfoSection(
                            title = "Descripción",
                            content = petDetail?.description ?: "no description"
                        )
                        InfoSection(
                            title = "Dueña/o",
                            content = petDetail?.owner ?: "sin dueña/o"
                        )
                        InfoSection(
                            title = "Reportante",
                            content = petDetail?.reporter ?: "sin reportante"
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
    MainContainer()
}