package com.example.buscapet.add_pet.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.add_pet.domain.use_case.AnalyzePetImageUseCase
import com.example.buscapet.add_pet.domain.use_case.ValidatePetImageUseCase
import com.example.buscapet.core.data.util.BitmapUtils
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.presentation.model.UiText
import com.example.buscapet.core.presentation.util.CoreUiEvent
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.report.domain.use_case.ValidateTextFieldUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petRepository: PetsRepository,
    private val validateTextField: ValidateTextFieldUseCase,
    private val validateImage: ValidatePetImageUseCase,
    private val analyzePetImage: AnalyzePetImageUseCase,
    private val bitmapUtils: BitmapUtils
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val currentUserId = currentUser?.uid

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    var formState by mutableStateOf(AddPetFormState())

    sealed interface AddPetUiEvent {
        data class ShowCoreEvent(val event: CoreUiEvent) : AddPetUiEvent
        object SuccessNavigate : AddPetUiEvent
    }

    private val _uiEvent = Channel<AddPetUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddPetEvent) {
        when (event) {
            is AddPetEvent.OnImageChanged -> {
                val imageString = event.image.toString()
                formState = formState.copy(addPetImage = imageString)
                analyzeImage(imageString)
            }

            is AddPetEvent.OnNameChanged -> formState = formState.copy(addPetName = event.name)
            is AddPetEvent.OnBreedChanged -> formState = formState.copy(addPetBreed = event.breed)
            is AddPetEvent.OnAgeChanged -> formState = formState.copy(addPetAge = event.age)
            is AddPetEvent.OnBirthChanged -> formState = formState.copy(addPetBirth = event.birth)
            is AddPetEvent.Submit -> submittData()
        }
    }

    private fun analyzeImage(uriString: String) {
        viewModelScope.launch {
            val bitmap = bitmapUtils.getBitmapFromUri(uriString)
            if (bitmap != null) {
                val results = analyzePetImage(bitmap)
                if (results.isNotEmpty()) {
                    val topResult = results.first()
                    // Auto-fill breed if empty
                    if (formState.addPetBreed.isNullOrBlank()) {
                         formState = formState.copy(addPetBreed = topResult.label)
                    }
                }
            }
        }
    }

    private fun submittData() {
        // ===== V a l i d a t i o n =====
        val imageResult = validateImage(formState.addPetImage)
        val nameResult = validateTextField(formState.addPetName)
        val breedResult = validateTextField(formState.addPetBreed)
        val ageResult = validateTextField(formState.addPetAge)
        val birthResult = validateTextField(formState.addPetBirth)

        // ===== S e a r c h   f o r   e r r o r ======
        val hasError = listOf(
            nameResult,
            breedResult,
            ageResult,
            birthResult
        ).any { !it.isValid }
        val hasImageError = !imageResult.isValid

        // ===== Set the results for view through formState ======
        if (hasError || hasImageError) {
            formState = formState.copy(
                addPetNameError = nameResult.error,
                addPetBreedError = breedResult.error,
                addPetAgeError = ageResult.error,
                addPetBirth = birthResult.error,
                addPetImageError = imageResult.error
            )
        } else {
            savePet()
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val inputEnable: Boolean = true
    )

    private fun toggleLoadingState(isLoading: Boolean) =
        _uiState.update { it.copy(loading = isLoading, inputEnable = !isLoading) }

    private fun savePet() {
        toggleLoadingState(true)
        viewModelScope.launch {
            // Convert URI to Base64 to persist it in Firestore
            val imageUri = formState.addPetImage
            val base64Image = if (imageUri != null) {
                bitmapUtils.convertUriToBase64(imageUri)
            } else {
                null
            }

            if (base64Image == null && imageUri != null) {
                toggleLoadingState(false)
                _uiEvent.send(
                    AddPetUiEvent.ShowCoreEvent(
                        CoreUiEvent.ShowSnackbar(
                            UiText.DynamicString("Error al procesar la imagen")
                        )
                    )
                )
                return@launch
            }

            val pet = Pet(
                name = formState.addPetName,
                breed = formState.addPetBreed,
                age = formState.addPetAge,
                birthDate = formState.addPetBirth,
                image = base64Image, // Save the Base64 string, not the URI
                petState = PetState.HOME,
                ownerId = currentUserId
            )
            
            val response = petRepository.insertPet(pet)
            if (response) {
                _uiEvent.send(AddPetUiEvent.SuccessNavigate)
            } else {
                toggleLoadingState(false)
                _uiEvent.send(
                    AddPetUiEvent.ShowCoreEvent(
                        CoreUiEvent.ShowSnackbar(
                            UiText.DynamicString("Error al guardar la mascota")
                        )
                    )
                )
            }
        }
    }

}