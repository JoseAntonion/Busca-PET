package com.example.buscapet.last_resports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LastReportsViewModel @Inject constructor(
    petsRepository: PetsRepository
) : ViewModel() {
    companion object {
        private const val MILLIS = 5_000L
    }

    data class PetUiState(
        val pets: List<Pet> = emptyList()
    )

    val petsUiState: StateFlow<PetUiState> =
        petsRepository.getAllPets()
            .map { PetUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(MILLIS),
                initialValue = PetUiState()
            )
}