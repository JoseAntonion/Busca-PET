package com.example.buscapet.add_pet.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.add_pet.domain.use_case.ValidatePetImageUseCase
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.domain.model.ValidationEvent
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
    private val validateImage: ValidatePetImageUseCase
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser?.displayName

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    var formState by mutableStateOf(AddPetFormState())

    private val validationFormChannel = Channel<ValidationEvent>()
    val validationEvents = validationFormChannel.receiveAsFlow()

    fun onEvent(event: AddPetEvent) {
        when (event) {
            is AddPetEvent.OnImageChanged -> {
                val imageString = event.image.toString()
                formState = formState.copy(addPetImage = imageString)
            }

            is AddPetEvent.OnNameChanged -> formState = formState.copy(addPetName = event.name)
            is AddPetEvent.OnBreedChanged -> formState = formState.copy(addPetBreed = event.breed)
            is AddPetEvent.OnAgeChanged -> formState = formState.copy(addPetAge = event.age)
            is AddPetEvent.OnBirthChanged -> formState = formState.copy(addPetBirth = event.birth)
            is AddPetEvent.Submit -> submittData()
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
            viewModelScope.launch {
                //validationFormChannel.send(ValidationEvent.Success)
                savePet()
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val inputEnable: Boolean = true,
        val inserted: Boolean = false
    )

    private fun toggleLoadingState() =
        _uiState.update { state -> state.copy(loading = !state.loading) }


    private fun dataAreInserted(value: Boolean) =
        _uiState.update { state -> state.copy(inserted = value) }

    private fun toggleInputEnableState() =
        _uiState.update { it.copy(inputEnable = !it.inputEnable) }


    private suspend fun savePet() {
        val pet = Pet(
            name = formState.addPetName,
            breed = formState.addPetBreed,
            age = formState.addPetAge,
            birthday = formState.addPetBirth,
            image = formState.addPetImage,
            petState = PetState.HOME,
            owner = currentUser
        )
        toggleLoadingState()
        toggleInputEnableState()
        viewModelScope.launch {
            val response = petRepository.insertPet(pet)
            if (response > 0)
                dataAreInserted(true)
        }
    }

}