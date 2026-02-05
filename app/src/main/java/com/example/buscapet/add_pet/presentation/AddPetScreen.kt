package com.example.buscapet.add_pet.presentation

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    navController: NavController = rememberNavController(),
    viewModel: AddPetViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState = viewModel.formState
    val snackbarHostState = remember { SnackbarHostState() }
    val openDatePicker = remember { mutableStateOf(false) }

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
        MainContainer(
            navController = navController,
            formState = formState,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState,
            onDatePickerClick = { openDatePicker.value = true }
        )
        CommonLoadingOverlay(
            isLoading = uiState.loading,
            message = "Guardando mascota..."
        )
    }

    if (openDatePicker.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
        DatePickerDialog(
            onDismissRequest = { openDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDatePicker.value = false
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onEvent(AddPetEvent.OnBirthDateChanged(it))
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDatePicker.value = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun MainContainer(
    navController: NavController = rememberNavController(),
    formState: AddPetFormState,
    viewModel: AddPetViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onDatePickerClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val checkupPlans = mapOf(3 to "Cada 3 meses", 6 to "Cada 6 meses", 12 to "Cada 12 meses")

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
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(Alignment.CenterVertically),
                                    model = formState.addPetImage,
                                    placeholder = painterResource(R.drawable.dummy_puppy),
                                    contentDescription = "imagen de la mascota"
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                CommonOutlinedTextFieldWithValidation(
                                    label = "Nombre",
                                    value = formState.addPetName ?: "",
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
                                    value = formState.addPetBreed ?: "",
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

                                Box(modifier = Modifier.clickable(onClick = onDatePickerClick)) {
                                    CommonOutlinedTextFieldWithValidation(
                                        label = "Fecha de Nacimiento",
                                        value = formState.addPetBirthDateFormatted ?: "",
                                        enabled = false, // To make it non-editable
                                        isError = formState.addPetBirthDateError != null,
                                        errorMessage = formState.addPetBirthDateError,
                                        keyOption = KeyboardOptions.Default,
                                        onValueChange = {}
                                    )
                                }

                                CheckupPlanDropdown(
                                    selectedPlan = formState.addPetCheckupPlan,
                                    onPlanSelected = { plan ->
                                        viewModel.onEvent(AddPetEvent.OnCheckupPlanChanged(plan))
                                    },
                                    plans = checkupPlans,
                                    isError = formState.addPetCheckupPlanError != null,
                                    errorMessage = formState.addPetCheckupPlanError
                                )
                            }
                        }

                        Button(
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 14.dp)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckupPlanDropdown(
    selectedPlan: Int?,
    onPlanSelected: (Int) -> Unit,
    plans: Map<Int, String>,
    isError: Boolean,
    errorMessage: String?
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = plans[selectedPlan] ?: "",
                onValueChange = {},
                label = { Text("Plan de control sano") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                isError = isError
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                plans.forEach { (planValue, planText) ->
                    DropdownMenuItem(
                        text = { Text(planText) },
                        onClick = {
                            onPlanSelected(planValue)
                            expanded = false
                        }
                    )
                }
            }
        }
        if (isError) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
private fun PreviewAddPet() {
    MainContainer(
        formState = AddPetFormState(),
        snackbarHostState = SnackbarHostState(),
        onDatePickerClick = {}
    )
}
