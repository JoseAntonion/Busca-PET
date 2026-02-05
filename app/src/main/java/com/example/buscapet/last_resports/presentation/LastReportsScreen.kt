package com.example.buscapet.last_resports.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.navigation.DetailReport
import com.example.buscapet.core.presentation.CommonCardView
import com.example.buscapet.core.presentation.CommonEmptyList
import com.example.buscapet.home.presentation.components.ReportListItemSkeleton
import com.example.buscapet.ui.theme.BuscaPetTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LastReportsScreen(
    navController: NavController = rememberNavController(),
    viewModel: LastReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.petsUiState.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    MainView(
        petList = uiState.pets,
        isLoading = uiState.isLoading,
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refresh() },
        navController = navController
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(
    petList: List<Pet> = emptyList(),
    isLoading: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    navController: NavController
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    BuscaPetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isLoading) {
                        items(5) {
                            ReportListItemSkeleton()
                        }
                    } else if (petList.isEmpty()) {
                        item {
                            CommonEmptyList(
                                painter = painterResource(id = R.drawable.ic_search),
                                text = "No se encontraron reportes"
                            )
                        }
                    } else {
                        items(petList) { pet ->
                            CommonCardView(
                                modifier = Modifier.padding(vertical = 8.dp),
                                title = pet.name ?: "Sin Nombre",
                                subtitle = pet.description ?: "Sin Descripción",
                                image = pet.image,
                                onClick = {
                                    navController.navigate(DetailReport(pet.id))
                                }
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewDark() {
    val petList = listOf(
        Pet(
            name = "Perro prueba",
            description = "Descripción del perro prueba"
        ),
        Pet(
            name = "Gato prueba",
            description = "Descripción del gato prueba"
        ),
    )
    MainView(
        petList = petList,
        navController = rememberNavController()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoading() {
    BuscaPetTheme {
        MainView(isLoading = true, navController = rememberNavController())
    }
}