package com.example.buscapet.medical_treatment.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Treatment
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalTreatmentViewModel @Inject constructor(
    private val petsRepository: PetsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = savedStateHandle.get<String>("petId")!!

    val treatments: StateFlow<List<Treatment>> = petsRepository.getTreatmentsForPet(petId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTreatment(medicationName: String, days: Int, frequencyHours: Int) {
        viewModelScope.launch {
            val treatment = Treatment(
                petId = petId,
                medicationName = medicationName,
                days = days,
                frequencyHours = frequencyHours
            )
            petsRepository.insertTreatment(treatment)
        }
    }

    fun updateTreatment(treatment: Treatment) {
        viewModelScope.launch {
            petsRepository.updateTreatment(treatment)
        }
    }

    fun deleteTreatment(treatment: Treatment) {
        viewModelScope.launch {
            petsRepository.deleteTreatment(treatment)
        }
    }
}