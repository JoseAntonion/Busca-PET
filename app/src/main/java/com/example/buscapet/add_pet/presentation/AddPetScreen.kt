package com.example.buscapet.add_pet.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.buscapet.R
import com.example.buscapet.core.presentation.AppBarWithBack
import com.example.buscapet.core.presentation.CommonLoadingOverlay
import com.example.buscapet.core.presentation.CommonOutlinedTextFieldWithValidation
import com.example.buscapet.core.presentation.util.ObserveAsEvents
import com.example.buscapet.ui.theme.BuscaPetTheme

var pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null

@Composable
fun AddPetScreen(
    navController: NavController = rememberNavController(),
    viewModel: AddPetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState = viewModel.formState
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(
        flow = viewModel.uiEvent,
        snackbarHostState = snackbarHostState
    ) { event ->
        when (event) {
            is AddPetViewModel.AddPetUiEvent.SuccessNavigate -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "user_message",
                    "Mascota guardada exitosamente"
                )
                navController.popBackStack()
            }
            is AddPetViewModel.AddPetUiEvent.ShowCoreEvent -> {
                // Handled automatically
            }
        }
    }

    pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null)
                viewModel.onEvent(AddPetEvent.OnImageChanged(uri))
        }

    Box(modifier = Modifier.fillMaxSize()) {
        MainContainter(
            navController = navController,
            uiState = uiState,
            formState = formState,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState
        )
        CommonLoadingOverlay(
            isLoading = uiState.loading,
            message = "Guardando mascota..."
        )
    }
}

@Composable
fun MainContainter(
    navController: NavController = rememberNavController(),
    uiState: AddPetViewModel.UiState,
    formState: AddPetFormState,
    viewModel: AddPetViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    BuscaPetTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                AppBarWithBack(
                    title = "Agregar nueva mascota",
                    onBackClick = { navController.navigateUp() }
                )
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
                        .fillMaxSize()
                        .padding(14.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = {
                        pickMedia?.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                        Text(text = "Seleccione una imagen de tu mascota")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    
                    if (formState.addPetImage != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        ) {
                            Column {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(Alignment.CenterVertically),
                                    model = formState.addPetImage,
                                    placeholder = painterResource(R.drawable.dummy_puppy),
                                    contentDescription = "imagen de la mascota"
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        CommonOutlinedTextFieldWithValidation(
                                            label = "Nombre",
                                            value = formState.addPetName,
                                            enabled = uiState.inputEnable,
                                            isError = formState.addPetNameError != null,
                                            errorMessage = formState.addPetNameError,
                                            keyOption = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next
                                            ),
                                            onValueChange = { text ->
                                                viewModel.onEvent(AddPetEvent.OnNameChanged(text))
                                            }
                                        )

                                        CommonOutlinedTextFieldWithValidation(
                                            label = "Raza",
                                            value = formState.addPetBreed,
                                            enabled = uiState.inputEnable,
                                            isError = formState.addPetBreedError != null,
                                            errorMessage = formState.addPetBreedError,
                                            keyOption = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next
                                            ),
                                            onValueChange = { text ->
                                                viewModel.onEvent(AddPetEvent.OnBreedChanged(text))
                                            }
                                        )

                                        CommonOutlinedTextFieldWithValidation(
                                            label = "Edad",
                                            value = formState.addPetAge,
                                            enabled = uiState.inputEnable,
                                            isError = formState.addPetAgeError != null,
                                            errorMessage = formState.addPetAgeError,
                                            keyOption = KeyboardOptions(
                                                keyboardType = KeyboardType.Number,
                                                imeAction = ImeAction.Next
                                            ),
                                            onValueChange = { text ->
                                                viewModel.onEvent(AddPetEvent.OnAgeChanged(text))
                                            }
                                        )

                                        CommonOutlinedTextFieldWithValidation(
                                            label = "Fecha de Nacimiento",
                                            value = formState.addPetBirth,
                                            enabled = uiState.inputEnable,
                                            isError = formState.addPetBirthError != null,
                                            errorMessage = formState.addPetBirthError,
                                            keyOption = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Done
                                            ),
                                            onValueChange = { text ->
                                                viewModel.onEvent(AddPetEvent.OnBirthChanged(text))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            modifier = Modifier
                                .padding(bottom = 14.dp)
                                .align(Alignment.End),
                            onClick = { viewModel.onEvent(AddPetEvent.Submit) },
                            enabled = !uiState.loading
                        ) {
                             Text(
                                 text = "Guardar",
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
private fun PreviewAddPet() {
    MainContainter(
        formState = AddPetFormState(),
        uiState = AddPetViewModel.UiState(loading = false),
        snackbarHostState = SnackbarHostState()
    )
}