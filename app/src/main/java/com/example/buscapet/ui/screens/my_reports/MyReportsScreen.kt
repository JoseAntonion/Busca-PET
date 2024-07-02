package com.example.buscapet.ui.screens.my_reports

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.domain.model.Pet
import com.example.buscapet.ui.commons.CommonCardView
import com.example.buscapet.ui.navigation.DetailReport
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyReportsScreen(
    navController: NavController = rememberNavController(),
    viewModel: MyReportsViewModel = hiltViewModel()
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    val uiState by viewModel.uiState.collectAsState()
    MainView(
        petList = uiState.myPets,
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