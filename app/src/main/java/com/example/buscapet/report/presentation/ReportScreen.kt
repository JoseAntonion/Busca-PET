package com.example.buscapet.report.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.buscapet.core.presentation.AppBarWithBack
import com.example.buscapet.core.presentation.CommonLoadingOverlay
import com.example.buscapet.core.presentation.util.CoreUiEvent
import com.example.buscapet.core.presentation.util.ObserveAsEvents
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val formState = viewModel.formState
    val isEditing = viewModel.isEditing
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Image selection state
    var showImageSourceOption by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    
    var tempUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            viewModel.onEvent(ReportEvent.OnImageChanged(tempUri!!))
        }
        showImageSourceOption = false
    }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
             viewModel.onEvent(ReportEvent.OnImageChanged(uri))
        }
        showImageSourceOption = false
    }

    fun launchCamera() {
        val file = File(context.cacheDir, "report_pet_image_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        tempUri = uri
        cameraLauncher.launch(uri)
    }

    fun fetchLocation() {
        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        viewModel.onEvent(ReportEvent.OnLocationRetrieved(location.latitude, location.longitude))
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false || 
                              permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (locationGranted) {
            fetchLocation()
        }
        
        // Auto-launch camera ONLY if NOT editing and image is empty
        if (cameraGranted && formState.petImage == null && !isEditing) {
             launchCamera()
        }
    }

    ObserveAsEvents(
        flow = viewModel.uiEvent,
        snackbarHostState = snackbarHostState
    ) { event ->
        when (event) {
            is ReportViewModel.ReportUiEvent.SuccessNavigate -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "user_message",
                    if (isEditing) "Reporte actualizado exitosamente" else "Reporte enviado exitosamente"
                )
                navController.popBackStack()
            }
            is ReportViewModel.ReportUiEvent.ShowCoreEvent -> {
                val coreEvent = event.event
                if (coreEvent is CoreUiEvent.ShowSnackbar) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = coreEvent.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    BuscaPetTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                AppBarWithBack(
                    title = if (isEditing) "Editar Reporte" else "Reportar Mascota Perdida",
                    onBackClick = { navController.navigateUp() }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    
                    // Main Image Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clickable { showImageSourceOption = true },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (formState.petImage != null) {
                                AsyncImage(
                                    model = formState.petImage,
                                    contentDescription = "Captured Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                // Edit Icon Overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    IconButton(
                                        onClick = { showImageSourceOption = true },
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            // .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Image",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Add Photo",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Agregar Foto")
                                }
                            }
                        }
                    }
                    
                    if (formState.petImageError != null) {
                         Text(
                             text = formState.petImageError!!,
                             color = MaterialTheme.colorScheme.error,
                             modifier = Modifier.padding(top = 8.dp)
                         )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Form Fields
                    
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = { viewModel.onEvent(ReportEvent.OnNameChanged(it)) },
                        label = { Text("Nombre de la mascota (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = formState.description,
                        onValueChange = { viewModel.onEvent(ReportEvent.OnDescriptionChanged(it)) },
                        label = { Text("Descripción / Detalles adicionales") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.onEvent(ReportEvent.Submit) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.loading
                    ) {
                        Text(if (isEditing) "Guardar Cambios" else "Confirmar y Enviar Reporte")
                    }
                    
                    if (formState.currentLatitude != null) {
                         Text(
                             text = "Ubicación: ${formState.currentLatitude}, ${formState.currentLongitude}",
                             style = MaterialTheme.typography.bodySmall,
                             modifier = Modifier.padding(top = 16.dp)
                         )
                    } else {
                         Text(
                             text = "Obteniendo ubicación...",
                             style = MaterialTheme.typography.bodySmall,
                             modifier = Modifier.padding(top = 16.dp)
                         )
                    }
                }
                
                CommonLoadingOverlay(
                    isLoading = uiState.loading,
                    message = if (isEditing) "Actualizando reporte..." else "Enviando reporte..."
                )
                
                if (showImageSourceOption) {
                    ModalBottomSheet(
                        onDismissRequest = { showImageSourceOption = false },
                        sheetState = sheetState
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .padding(bottom = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Seleccionar Imagen",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { launchCamera() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Camera",
                                        modifier = Modifier
                                            .size(64.dp)
                                            .padding(8.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text("Cámara")
                                }
                                
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { galleryLauncher.launch("image/*") }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoLibrary,
                                        contentDescription = "Gallery",
                                        modifier = Modifier
                                            .size(64.dp)
                                            .padding(8.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text("Galería")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}