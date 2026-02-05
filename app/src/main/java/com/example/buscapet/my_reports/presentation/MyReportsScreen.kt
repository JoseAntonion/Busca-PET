package com.example.buscapet.my_reports.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun MyReportsScreen(
    navController: NavController = rememberNavController(),
    viewModel: MyReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MainView(
        petList = uiState.myPets,
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
            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
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
                                painter = painterResource(id = R.drawable.ic_empty),
                                text = "No tienes reportes creados"
                            )
                        }
                    } else {
                        items(petList) { pet ->
                            CommonCardView(
                                modifier = Modifier.padding(vertical = 8.dp),
                                title = pet.name,
                                subtitle = pet.description,
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
fun Preview() {
    val petList = listOf(
        Pet(name = "Perro prueba", description = "Descripción del perro prueba"),
        Pet(name = "Gato prueba", description = "Descripción del gato prueba"),
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