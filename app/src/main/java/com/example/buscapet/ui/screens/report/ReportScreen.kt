package com.example.buscapet.ui.screens.report

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.data.local.Pet
import com.example.buscapet.ui.commons.AppBarWithBack
import com.example.buscapet.ui.commons.CommonFilledTextField
import com.example.buscapet.ui.theme.BuscaPetTheme
import kotlinx.coroutines.launch

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: ReportViewModel = hiltViewModel()
) {
    MainContainter(navController = navController, viewModel = viewModel)
}

@Composable
fun MainContainter(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    BuscaPetTheme {
        Scaffold(
            topBar = {
                AppBarWithBack(
                    title = "Reportar mascota perdida",
                    onBackClick = { navController.popBackStack() }
                )
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
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Datos de la mascota",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val nameInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Nombre de la mascota",
                        inputText = nameInput
                    )
                    val ageInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Edad de la mascota",
                        inputText = ageInput
                    )
                    val breedInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Raza de la mascota",
                        inputText = breedInput
                    )
                    val descriptionInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Descripcion",
                        inputText = descriptionInput
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.savePet(
                                    Pet(
                                        name = nameInput.value,
                                        age = ageInput.value,
                                        breed = breedInput.value,
                                        description = descriptionInput.value
                                    )
                                )
                            }
                        }
                    ) {
                        Text(text = "Reportar")
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
private fun Preview() {
    MainContainter(navController = rememberNavController())
}