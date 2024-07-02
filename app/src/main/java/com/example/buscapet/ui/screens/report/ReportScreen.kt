package com.example.buscapet.ui.screens.report

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.domain.model.Pet
import com.example.buscapet.ui.commons.AppBarWithBack
import com.example.buscapet.ui.commons.CommonFilledTextField
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReportScreen(
    navController: NavController = rememberNavController(),
    viewModel: ReportViewModel = hiltViewModel()
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserName = currentUser?.displayName
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val lContext = LocalContext.current

    LaunchedEffect(key1 = state.inserted) {
        if (state.inserted) {
            delay(2000L)
            Toast.makeText(lContext, "Reporte guardado", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }
    MainContainter(
        navController = navController,
        state = state,
        currentUserName = currentUserName,
        onReportClick = { pet ->
            if (pet.name?.isEmpty() == true ||
                pet.breed?.isEmpty() == true ||
                pet.age?.isEmpty() == true ||
                pet.description?.isEmpty() == true ||
                pet.reporter?.isEmpty() == true
            ) {
                Toast.makeText(lContext, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.toggleInputEnableState()
                coroutineScope.launch {
                    viewModel.savePet(pet)
                }
            }
        }
    )
}

@Composable
fun MainContainter(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ReportViewModel.UiState,
    currentUserName: String? = null,
    onReportClick: (pet: Pet) -> Unit = {},
) {
    BuscaPetTheme {
        Scaffold(
            topBar = {
                AppBarWithBack(
                    title = "Reportar mascota perdida",
                    onBackClick = { navController.navigateUp() }
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

                    val nameInputValidation = remember { mutableStateOf(true) }
                    val nameInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Nombre de la mascota",
                        inputText = nameInput,
                        enabled = state.inputEnable,
                        isValid = nameInputValidation,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    val ageInputValidation = remember { mutableStateOf(true) }
                    val ageInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Edad de la mascota",
                        inputText = ageInput,
                        enabled = state.inputEnable,
                        isValid = ageInputValidation,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    val breedInputValidation = remember { mutableStateOf(true) }
                    val breedInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Raza de la mascota",
                        inputText = breedInput,
                        enabled = state.inputEnable,
                        isValid = breedInputValidation,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    val descriptionInputValidation = remember { mutableStateOf(true) }
                    val descriptionInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Descripcion",
                        inputText = descriptionInput,
                        enabled = state.inputEnable,
                        isValid = descriptionInputValidation,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                    val ownerInputValidation = remember { mutableStateOf(true) }
                    val ownerInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Dueño",
                        inputText = ownerInput,
                        enabled = state.inputEnable,
                        isValid = ownerInputValidation,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                    val reporterInputValidation = remember { mutableStateOf(true) }
                    val reporterInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Reportante",
                        inputText = reporterInput,
                        enabled = state.inputEnable,
                        isValid = reporterInputValidation,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 0.dp, 14.dp, 14.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(
                            onClick = {
                                onReportClick(
                                    Pet(
                                        name = nameInput.value,
                                        age = ageInput.value,
                                        breed = breedInput.value,
                                        description = descriptionInput.value,
                                        reporter = reporterInput.value.ifEmpty { currentUserName },
                                        petState = "perdido",
                                    )
                                )
                            },
                            enabled = !state.loading,
                            modifier = Modifier
                                .height(50.dp)
                                .width(120.dp)
                        ) {
                            if (state.loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(35.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    trackColor = MaterialTheme.colorScheme.primary,
                                )
                            } else
                                Text(
                                    text = "Reportar",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                        }
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
    MainContainter(
        navController = rememberNavController(),
        onReportClick = {},
        state = ReportViewModel.UiState(loading = false)
    )
}