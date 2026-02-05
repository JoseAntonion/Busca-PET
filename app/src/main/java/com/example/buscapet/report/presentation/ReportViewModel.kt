package com.example.buscapet.report.presentation

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.buscapet.add_pet.domain.use_case.AnalyzePetImageUseCase
import com.example.buscapet.core.data.util.BitmapUtils
import com.example.buscapet.core.domain.classification.ModelType
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.presentation.model.UiText
import com.example.buscapet.core.presentation.util.CoreUiEvent
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val petsRepository: PetsRepository,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
    private val analyzePetImage: AnalyzePetImageUseCase,
    private val bitmapUtils: BitmapUtils
) : AndroidViewModel(application) {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val currentUserId = currentUser?.uid

    var formState by mutableStateOf(ReportFormState())

    val isEditing: Boolean = savedStateHandle.get<String>("petId") != null

    sealed interface ReportUiEvent {
        data class ShowCoreEvent(val event: CoreUiEvent) : ReportUiEvent
        object SuccessNavigate : ReportUiEvent
    }

    private val _uiEvent = Channel<ReportUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val petId = savedStateHandle.get<String>("petId")
        if (petId != null) {
            viewModelScope.launch {
                val pet = petsRepository.getPetById(petId)
                if (pet != null) {
                    val imageUri = if (!pet.image.isNullOrBlank()) {
                        saveBase64ToCache(pet.image)
                    } else null

                    formState = formState.copy(
                        petId = pet.id,
                        petImage = imageUri?.toString(),
                        description = pet.description ?: "",
                        currentLatitude = pet.latitude,
                        currentLongitude = pet.longitude
                    )
                }
            }
        }
    }

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.OnImageChanged -> {
                formState = formState.copy(petImage = event.image.toString(), petImageError = null)
                analyzeAndSubmitReport(event.image.toString())
            }
            is ReportEvent.OnLocationRetrieved -> {
                formState = formState.copy(
                    currentLatitude = event.latitude,
                    currentLongitude = event.longitude
                )
            }
        }
    }

    private fun analyzeAndSubmitReport(uriString: String) {
        toggleLoadingState(true)
        viewModelScope.launch {
            try {
                val bitmap = bitmapUtils.getBitmapFromUri(uriString)
                if (bitmap == null) {
                    toggleLoadingState(false)
                    showError("Error al procesar la imagen")
                    return@launch
                }

                val results = analyzePetImage(bitmap, ModelType.ANIMAL_TYPE)
                val petType =
                    if (results.isNotEmpty()) results.first().label else "Mascota desconocida"

                saveReport(petType)

            } catch (e: Exception) {
                e.printStackTrace()
                toggleLoadingState(false)
                showError("Error inesperado: ${e.localizedMessage}")
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val inputEnable: Boolean = true
    )

    private fun toggleLoadingState(isLoading: Boolean) =
        _uiState.update { it.copy(loading = isLoading, inputEnable = !isLoading) }

    private suspend fun saveReport(petType: String) {
        val imageUriString = formState.petImage
        if (imageUriString == null) {
            formState = formState.copy(petImageError = "La imagen es obligatoria")
            toggleLoadingState(false)
            return
        }

        val base64Image = convertUriToBase64(imageUriString.toUri())
        if (base64Image == null) {
            toggleLoadingState(false)
            showError("Error al convertir la imagen")
            return
        }

        val petId = formState.petId ?: UUID.randomUUID().toString()
        val pet = Pet(
            id = petId,
            image = base64Image,
            petState = PetState.LOST,
            reporterId = currentUserId,
            latitude = formState.currentLatitude ?: 0.0,
            longitude = formState.currentLongitude ?: 0.0,
            name = "", // Name is no longer required
            description = petType // Description is now the pet type
        )

        val response = petsRepository.insertPet(pet)
        if (response) {
            _uiEvent.send(ReportUiEvent.SuccessNavigate)
        } else {
            toggleLoadingState(false)
            showError("Error al guardar el reporte en Firebase")
        }
    }

    private suspend fun saveBase64ToCache(base64String: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
                val file = java.io.File(application.cacheDir, "temp_edit_${System.currentTimeMillis()}.jpg")
                val fos = java.io.FileOutputStream(file)
                fos.write(imageBytes)
                fos.close()
                androidx.core.content.FileProvider.getUriForFile(
                    application,
                    "${application.packageName}.fileprovider",
                    file
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun convertUriToBase64(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = application.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap == null) return@withContext null

                // Resize if too big (max 800x800)
                val maxDimension = 800
                val ratio = Math.min(
                    maxDimension.toDouble() / bitmap.width,
                    maxDimension.toDouble() / bitmap.height
                )
                val width = (bitmap.width * ratio).toInt()
                val height = (bitmap.height * ratio).toInt()
                val resizedBitmap = bitmap.scale(width, height)

                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                val byteArray = outputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun showError(message: String) {
        _uiEvent.send(
            ReportUiEvent.ShowCoreEvent(
                CoreUiEvent.ShowSnackbar(
                    UiText.DynamicString(message)
                )
            )
        )
    }
}
