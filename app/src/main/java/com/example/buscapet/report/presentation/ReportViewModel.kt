package com.example.buscapet.report.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
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
class ReportViewModel @Inject constructor(
    private val petsRepository: PetsRepository,
    private val validateTextField: ValidateTextFieldUseCase
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser?.displayName
    var formState by mutableStateOf(ReportPetFormState())

    private val validationReportFormChannel = Channel<ValidationEvent> { }
    val validationEvents = validationReportFormChannel.receiveAsFlow()

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
    }

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.PetNameChanged -> {
                formState = formState.copy(petName = event.name)
            }

            is ReportEvent.PetBreedChanged -> {
                formState = formState.copy(petBreed = event.breed)
            }

            is ReportEvent.PetAgeChanged -> {
                formState = formState.copy(petAge = event.age)
            }

            is ReportEvent.PetDescriptionChanged -> {
                formState = formState.copy(petDescription = event.desc)
            }

            is ReportEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val nameResult = validateTextField(formState.petName)
        val ageResult = validateTextField(formState.petAge)
        val breedResult = validateTextField(formState.petBreed)
        val descriptionResult = validateTextField(formState.petDescription)

        val hasError = listOf(
            nameResult,
            ageResult,
            breedResult,
            descriptionResult
        ).any { !it.isValid }

        if (hasError) {
            formState = formState.copy(
                petNameError = nameResult.error,
                petAgeError = ageResult.error,
                petBreedError = breedResult.error,
                petDescriptionError = descriptionResult.error,
            )
        } else {
            viewModelScope.launch {
                validationReportFormChannel.send(ValidationEvent.Success)
                toggleLoadingState()
                toggleInputEnableState()
                savePetReport()
            }
        }

    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val inputEnable: Boolean = true,
        var isValid: Boolean = true,
        val inserted: Boolean = false
    )

    private fun toggleLoadingState() =
        _uiState.update { it.copy(loading = !it.loading) }

    private fun toggleInputEnableState() =
        _uiState.update { it.copy(inputEnable = !it.inputEnable) }

    private suspend fun savePetReport() {
        val newPet = Pet(
            name = formState.petName,
            breed = formState.petBreed,
            age = formState.petAge,
            description = formState.petDescription,
            reporter = currentUser,
            petState = PetState.LOST
        )
        viewModelScope.launch {
            val response = petsRepository.insertPet(newPet)
            if (response > 0)
                _uiState.update { it.copy(inserted = true) }
        }
    }

}