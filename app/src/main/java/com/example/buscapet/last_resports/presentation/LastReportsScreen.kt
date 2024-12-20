package com.example.buscapet.last_resports.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.presentation.CommonCardView
import com.example.buscapet.core.presentation.CommonEmptyList
import com.example.buscapet.core.navigation.DetailReport
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LastReportsScreen(
    navController: NavController = rememberNavController(),
    viewModel: LastReportsViewModel = hiltViewModel()
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    val allPets by viewModel.petsUiState.collectAsState()
    MainView(
        petList = allPets.pets,
        navController = navController
    )
}

@Composable
fun MainView(
    petList: List<Pet> = emptyList(),
    navController: NavController
) {
    BuscaPetTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (petList.isEmpty()) {
                    item {
                        CommonEmptyList(
                            painter = painterResource(id = R.drawable.ic_search),
                            text = "No se encontraron reportes"
                        )
                    }
                } else {
                    items(petList) { pet ->
                        CommonCardView(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            title = pet.name,
                            subtitle = pet.name,
                            onClick = {
                                navController.navigate(DetailReport(pet.id))
                            }
                        )
                    }
                }
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
            breed = "Raza pulenta",
            age = "5 años",
            description = "Descripción del perro prueba"
        ),
        Pet(
            name = "Gato prueba",
            breed = "Raza pulenta",
            age = "5 años",
            description = "Descripción del gato prueba"
        ),
        Pet(
            name = "Pez prueba",
            breed = "Raza pulenta",
            age = "5 años",
            description = "Descripción del pez prueba"
        )
    )
    MainView(
        petList = petList,
        rememberNavController()
    )
}