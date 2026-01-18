package com.example.buscapet.my_pets.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.presentation.CommonCardView
import com.example.buscapet.core.navigation.AddPet
import com.example.buscapet.core.navigation.DetailReport
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun MyPetsScreen(
    navController: NavController = rememberNavController(),
    viewModel: MyPetsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.lostPets.collectAsState()

    MainContainer(
        petList = uiState.petList,
        navController = navController
    )
}

@Composable
fun MainContainer(
    petList: List<Pet> = emptyList(),
    navController: NavController = rememberNavController()
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
                item {
                    CommonCardView(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        title = "Agregar mascota",
                        subtitle = "Agrega una nueva mascota",
                        isEmpty = true,
                        onClick = {
                            navController.navigate(AddPet)
                        }
                    )
                }

                items(petList) { pet ->
                    CommonCardView(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        title = pet.name,
                        subtitle = pet.name,
                        image = pet.image,
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
    MainContainer(
        petList = emptyList<Pet>(),
        rememberNavController()
    )
}