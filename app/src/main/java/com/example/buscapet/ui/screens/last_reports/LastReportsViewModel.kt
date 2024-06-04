package com.example.buscapet.ui.screens.last_reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.data.local.Pet
import com.example.buscapet.data.local.PetUiState
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LastReportsViewModel @Inject constructor(private val petsRepository: PetsRepository) :
    ViewModel() {
    companion object {
        private const val MILLIS = 5_000L
    }

    val petsUiState: StateFlow<PetUiState> =
        petsRepository.getAllPets()
            .map { PetUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(MILLIS),
                initialValue = PetUiState()
            )

    suspend fun savePet(pet: Pet) {
        viewModelScope.launch {
            petsRepository.insertPet(pet)
        }
    }
}