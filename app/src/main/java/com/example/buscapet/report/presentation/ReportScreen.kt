package com.example.buscapet.report.presentation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.core.presentation.AppBarWithBack
import com.example.buscapet.core.presentation.CommonOutlinedTextFieldWithValidation
import com.example.buscapet.ui.theme.BuscaPetTheme
import kotlinx.coroutines.delay

@Composable
fun ReportScreen(
    navController: NavController = rememberNavController(),
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState = viewModel.formState
    val lContext = LocalContext.current

    LaunchedEffect(key1 = lContext) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ReportViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        lContext,
                        "Reporte Válido",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(key1 = uiState.inserted) {
        if (uiState.inserted) {
            delay(2000L)
            Toast.makeText(lContext, "Reporte guardado", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    MainContainter(
        navController = navController,
        uiState = uiState,
        viewModel = viewModel,
        formState = formState
    )
}

@Composable
fun MainContainter(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: ReportViewModel.UiState,
    formState: ReportPetFormState = ReportPetFormState(),
    viewModel: ReportViewModel = hiltViewModel(),
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

                    CommonOutlinedTextFieldWithValidation(
                        label = stringResource(id = R.string.report_form_pet_name_label),
                        value = formState.petName,
                        onValueChange = { text: String ->
                            viewModel.onEvent(ReportEvent.PetNameChanged(text))
                        },
                        enabled = uiState.inputEnable,
                        isError = formState.petNameError != null,
                        errorMessage = formState.petNameError,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CommonOutlinedTextFieldWithValidation(
                        label = stringResource(id = R.string.report_form_pet_age_label),
                        value = formState.petAge,
                        onValueChange = { text: String ->
                            viewModel.onEvent(ReportEvent.PetAgeChanged(text))
                        },
                        enabled = uiState.inputEnable,
                        isError = formState.petAgeError != null,
                        errorMessage = formState.petAgeError,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    CommonOutlinedTextFieldWithValidation(
                        label = stringResource(id = R.string.report_form_pet_breed_label),
                        value = formState.petBreed,
                        onValueChange = { text: String ->
                            viewModel.onEvent(ReportEvent.PetBreedChanged(text))
                        },
                        enabled = uiState.inputEnable,
                        isError = formState.petBreedError != null,
                        errorMessage = formState.petBreedError,
                        keyOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CommonOutlinedTextFieldWithValidation(
                        label = stringResource(id = R.string.report_form_pet_desc_label),
                        value = formState.petDescription,
                        onValueChange = { text: String ->
                            viewModel.onEvent(ReportEvent.PetDescriptionChanged(text))
                        },
                        enabled = uiState.inputEnable,
                        isError = formState.petDescriptionError != null,
                        errorMessage = formState.petDescriptionError,
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
                            onClick = { viewModel.onEvent(ReportEvent.Submit) },
                            enabled = !uiState.loading,
                            modifier = Modifier
                                .height(50.dp)
                                .width(120.dp)
                        ) {
                            if (uiState.loading) {
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
        uiState = ReportViewModel.UiState(loading = false)
    )
}