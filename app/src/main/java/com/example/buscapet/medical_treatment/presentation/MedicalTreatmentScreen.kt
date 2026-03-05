package com.example.buscapet.medical_treatment.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.buscapet.core.domain.model.Treatment
import com.example.buscapet.core.presentation.AppBarWithBack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalTreatmentScreen(
    navController: NavController,
    viewModel: MedicalTreatmentViewModel = hiltViewModel()
) {
    val treatments by viewModel.treatments.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedTreatment by remember { mutableStateOf<Treatment?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppBarWithBack(
                title = "Tratamiento Médico",
                onBackClick = { navController.navigateUp() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedTreatment = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tratamiento")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (treatments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No current treatment exists. Add a new treatment using the + button",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = treatments,
                    key = { it.id }
                ) { treatment ->
                    SwipeToDeleteContainer(
                        item = treatment,
                        onDelete = {
                            viewModel.deleteTreatment(treatment)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "The treatment was deleted successfully",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    ) {
                        TreatmentCard(
                            treatment = treatment,
                            onEditClick = {
                                selectedTreatment = it
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddEditTreatmentDialog(
                treatment = selectedTreatment,
                onDismiss = { showDialog = false },
                onConfirm = { name, days, hours ->
                    if (selectedTreatment == null) {
                        viewModel.addTreatment(name, days, hours)
                    } else {
                        val updatedTreatment = selectedTreatment!!.copy(
                            medicationName = name,
                            days = days,
                            frequencyHours = hours
                        )
                        viewModel.updateTreatment(updatedTreatment)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    item: Treatment,
    onDelete: (Treatment) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (Treatment) -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd || value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        },
        positionalThreshold = { totalDistance ->
            totalDistance * 0.5f // Set threshold to 50%
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        // Apply clip to the SwipeToDismissBox to ensure background matches card corners
        Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
            SwipeToDismissBox(
                state = state,
                backgroundContent = {
                    val color = Color.Red
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                },
                content = { content(item) }
            )
        }
    }
}

@Composable
fun TreatmentCard(treatment: Treatment, onEditClick: (Treatment) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp) // Explicitly set shape to match container clip
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = treatment.medicationName, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Duración: ${treatment.days} días",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Frecuencia: Cada ${treatment.frequencyHours} horas",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { onEditClick(treatment) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    }
}

@Composable
fun AddEditTreatmentDialog(
    treatment: Treatment?,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf(treatment?.medicationName ?: "") }
    var days by remember { mutableStateOf(treatment?.days?.toString() ?: "") }
    var hours by remember { mutableStateOf(treatment?.frequencyHours?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (treatment == null) "Agregar Medicamento" else "Editar Medicamento") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del medicamento") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = days,
                    onValueChange = { days = it },
                    label = { Text("Días de tratamiento") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = hours,
                    onValueChange = { hours = it },
                    label = { Text("Cada cuantas horas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, days.toIntOrNull() ?: 0, hours.toIntOrNull() ?: 0)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}