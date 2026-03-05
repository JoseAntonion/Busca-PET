package com.example.buscapet.detail_report.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.core.domain.model.Caracteristic
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.navigation.AddPet
import com.example.buscapet.core.navigation.MedicalTreatment
import com.example.buscapet.core.navigation.Report
import com.example.buscapet.core.navigation.ReportMap
import com.example.buscapet.core.presentation.AppBarWithBack
import com.example.buscapet.core.presentation.CommonLoadingOverlay
import com.example.buscapet.core.presentation.util.Base64Image
import com.example.buscapet.core.presentation.util.ObserveAsEvents
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DetailReportScreen(
    viewModel: DetailReportViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    petId: String,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(
        flow = viewModel.uiEvent,
        snackbarHostState = snackbarHostState
    ) { event ->
        when (event) {
            is DetailReportViewModel.DetailUiEvent.DeleteSuccess -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "user_message",
                    "Reporte eliminado exitosamente"
                )
                navController.popBackStack()
            }
            is DetailReportViewModel.DetailUiEvent.EditPet -> {
                navController.navigate(AddPet(petId = event.petId))
            }
            is DetailReportViewModel.DetailUiEvent.EditReport -> {
                navController.navigate(Report(petId = event.petId))
            }
            is DetailReportViewModel.DetailUiEvent.ShowCoreEvent -> {
                // Handled automatically
            }
        }
    }

    val petDetail by viewModel.petDetails.collectAsState()
    val routeCoordinates by viewModel.routeCoordinates.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFetching by viewModel.isFetching.collectAsState()
    val isOwner by viewModel.isOwner.collectAsState()
    val hasActiveTreatment by viewModel.hasActiveTreatment.collectAsState()
    val nextMedication by viewModel.nextMedication.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        MainContainer(
            navController = navController,
            currentPet = petDetail,
            routeCoordinates = routeCoordinates,
            isFetching = isFetching,
            isOwner = isOwner,
            hasActiveTreatment = hasActiveTreatment,
            nextMedication = nextMedication,
            snackbarHostState = snackbarHostState,
            onDeletePetClick = { viewModel.onDeletePet() },
            onEditPetClick = { viewModel.onEditPet() },
            onNavigateToTreatment = {
                navController.navigate(MedicalTreatment(petId = petId))
            }
        )
        CommonLoadingOverlay(
            isLoading = isLoading,
            message = "Eliminando reporte..."
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    navController: NavController = rememberNavController(),
    currentPet: Pet? = null,
    routeCoordinates: List<LatLng> = emptyList(),
    isFetching: Boolean = false,
    isOwner: Boolean = false,
    hasActiveTreatment: Boolean = false,
    nextMedication: String? = null,
    onDeletePetClick: () -> Unit = {},
    onEditPetClick: () -> Unit = {},
    onNavigateToTreatment: () -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    var showMedicalRecordDialog by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppBarWithBack(
                title = currentPet?.name ?: "",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFetching) {
                                CircularProgressIndicator()
                            } else {
                                Base64Image(
                                    base64String = currentPet?.image,
                                    contentDescription = "Pet detail image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    placeholder = {
                                        Image(
                                            painter = painterResource(id = R.drawable.dummy_puppy),
                                            contentDescription = "Pet detail image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                )
                                if (isOwner || hasActiveTreatment) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(onClick = { onDeletePetClick() }) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = null,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                        if (isOwner) {
                                            Card(onClick = { onEditPetClick() }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Edit,
                                                    contentDescription = null,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    PetInfoSection(
                        pet = currentPet,
                        isOwner = isOwner,
                        onMedicalRecordClick = { showMedicalRecordDialog = true }
                    )
                }

                if (isOwner && currentPet?.petState == PetState.HOME) {
                    item {
                        Button(
                            onClick = onNavigateToTreatment,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Tratamiento Médico")
                        }
                    }
                    nextMedication?.let {
                        item {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (currentPet?.latitude != null && currentPet.longitude != null) {
                    item {
                        MapPreview(
                            latitude = currentPet.latitude,
                            longitude = currentPet.longitude,
                            routeCoordinates = routeCoordinates,
                            onMapClick = {
                                navController.navigate(
                                    ReportMap(
                                        latitude = currentPet.latitude.toString(),
                                        longitude = currentPet.longitude.toString(),
                                        petName = currentPet.name ?: "Mascota"
                                    )
                                )
                            }
                        )
                    }
                }

                item {
                    Text(
                        modifier = Modifier.padding(vertical = 20.dp),
                        textAlign = TextAlign.Justify,
                        text = currentPet?.description ?: "Sin descripción",
                    )
                }

                if (currentPet?.caracteristics?.isNotEmpty() == true) {
                    items(currentPet.caracteristics) { caracteristic ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card {
                                Image(
                                    modifier = Modifier.padding(14.dp),
                                    imageVector = iconByName(caracteristic.icon),
                                    contentDescription = null
                                )
                            }
                            InfoSection(
                                title = caracteristic.name ?: "no caracteristic",
                                content = caracteristic.data ?: "no data"
                            )
                        }
                    }
                }
            }
        }
    }

    if (showMedicalRecordDialog && currentPet != null) {
        MedicalRecordDialog(
            pet = currentPet,
            onDismiss = { showMedicalRecordDialog = false },
            onModify = {
                showMedicalRecordDialog = false
                onEditPetClick()
            }
        )
    }
}

@Composable
fun PetInfoSection(
    pet: Pet?,
    isOwner: Boolean,
    onMedicalRecordClick: () -> Unit
) {
    if (pet == null) return
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (isOwner && pet.petState == PetState.HOME) {
            Button(
                onClick = onMedicalRecordClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ficha Médica")
            }
        } else {
            pet.birthDate?.let {
                InfoSection(title = "Edad", content = calculateAge(it))
            }
        }

        pet.lastCheckupDate?.let {
            InfoSection(
                title = "Próximo Control",
                content = calculateNextCheckup(it, pet.checkupPlan)
            )
        }
    }
}

@Composable
fun MedicalRecordDialog(
    pet: Pet,
    onDismiss: () -> Unit,
    onModify: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Ficha Médica de ${pet.name}") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Base64Image(
                    base64String = pet.image,
                    contentDescription = "Pet photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    placeholder = {
                        Image(
                            painter = painterResource(id = R.drawable.dummy_puppy),
                            contentDescription = "Pet photo placeholder",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MedicalRecordItem(label = "Edad", value = pet.birthDate?.let { calculateAge(it) } ?: "N/A")
                    MedicalRecordItem(label = "Fecha de nacimiento", value = pet.birthDate?.let { formatDate(it) } ?: "N/A")
                    MedicalRecordItem(label = "Peso", value = pet.weight?.let { "$it Kg" } ?: "N/A")
                    MedicalRecordItem(label = "Animal", value = pet.animalType ?: "N/A")
                    MedicalRecordItem(label = "Raza", value = pet.breed ?: "N/A")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onModify) {
                Text("Modificar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun MedicalRecordItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

private fun formatDate(date: Long): String {
    return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(java.util.Date(date))
}

private fun calculateAge(birthDate: Long): String {
    val dob = Calendar.getInstance().apply { timeInMillis = birthDate }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return if (age < 0) "0 años" else "$age años"
}

private fun calculateNextCheckup(lastCheckup: Long, plan: Int?): String {
    if (plan == null) return "No definido"
    val next = Calendar.getInstance().apply {
        timeInMillis = lastCheckup
        add(Calendar.MONTH, plan)
    }
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(next.time)
}


@Composable
fun MapPreview(
    latitude: Double,
    longitude: Double,
    routeCoordinates: List<LatLng>,
    onMapClick: () -> Unit
) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 12f)
    }
    var showAllCoordinates by remember { mutableStateOf(false) }

    val pointsToShow = if (showAllCoordinates) {
        routeCoordinates
    } else {
        routeCoordinates.takeLast(5)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ruta de avistamientos",
                style = MaterialTheme.typography.titleMedium
            )
            if (routeCoordinates.size > 5) {
                TextButton(onClick = { showAllCoordinates = !showAllCoordinates }) {
                    Text(if (showAllCoordinates) "Mostrar menos" else "Mostrar más")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        tiltGesturesEnabled = false,
                        rotationGesturesEnabled = false,
                        mapToolbarEnabled = false
                    )
                ) {
                    if (pointsToShow.isNotEmpty()) {
                        Polyline(points = pointsToShow, color = MaterialTheme.colorScheme.primary)
                        pointsToShow.forEach { pos ->
                            Marker(state = MarkerState(position = pos))
                        }
                    } else {
                        Marker(state = MarkerState(position = location))
                    }
                }
                // Overlay to capture clicks
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onMapClick() }
                )
            }
        }
    }
}

fun iconByName(name: String?): ImageVector {
    try {
        val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
        val method = cl.declaredMethods.first()
        return method.invoke(null, Icons.Filled) as ImageVector
    } catch (e: Exception) {
        return Icons.Filled.BrokenImage
    }
}

@Composable
fun InfoSection(
    title: String = "",
    content: String = ""
) {
    Box {
        Column(
            modifier = Modifier
                .padding(14.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
private fun Preview() {
    BuscaPetTheme {
        MainContainer(
            navController = rememberNavController(),
            currentPet = Pet(
                name = "Perro prueba",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
                        "aliquip ex ea commodo consequat.",
                caracteristics = listOf(
                    Caracteristic(
                        id = 1,
                        icon = "Cake",
                        name = "Cumpleaños",
                        data = "20 de Enero"
                    ),
                    Caracteristic(
                        id = 2,
                        icon = "Female",
                        name = "Genero",
                        data = "Hembra"
                    ),
                    Caracteristic(
                        id = 3,
                        icon = "CrueltyFree",
                        name = "Raza",
                        data = "raza"
                    ),
                    Caracteristic(
                        id = 4,
                        icon = "some",
                        name = "title",
                        data = "data"
                    ),
                    Caracteristic(
                        id = 5,
                        icon = "Camera",
                        name = "Edad",
                        data = "Sin Edad"
                    ),
                ),
                latitude = -33.4489,
                longitude = -70.6693
            ),
            isOwner = true, // Force true for preview
            snackbarHostState = SnackbarHostState()
        )
    }
}