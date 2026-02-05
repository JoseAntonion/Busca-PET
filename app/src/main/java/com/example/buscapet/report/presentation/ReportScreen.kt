package com.example.buscapet.report.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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

@SuppressLint("MissingPermission")
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val formState = viewModel.formState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var tempUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            viewModel.onEvent(ReportEvent.OnImageChanged(tempUri!!))
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onEvent(ReportEvent.OnImageChanged(uri))
        }
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
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (locationGranted) {
            fetchLocation()
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
                    "Reporte enviado exitosamente"
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
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                AppBarWithBack(
                    title = "Reportar Avistamiento",
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Reportar un avistamiento es rápido y fácil. Solo necesitas una foto.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (formState.petImage != null) {
                                AsyncImage(
                                    model = formState.petImage,
                                    contentDescription = "Imagen seleccionada para el reporte",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = "La imagen que selecciones aparecerá aquí",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    if (formState.petImageError != null) {
                        Text(
                            text = formState.petImageError,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(onClick = { launchCamera() }) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Tomar Foto",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Tomar Foto")
                        }
                        Button(onClick = { galleryLauncher.launch("image/*") }) {
                            Icon(
                                Icons.Default.PhotoLibrary,
                                contentDescription = "Seleccionar de Galería",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Galería")
                        }
                    }
                }

                CommonLoadingOverlay(
                    isLoading = uiState.loading,
                    message = "Enviando reporte..."
                )
            }
        }
    }
}