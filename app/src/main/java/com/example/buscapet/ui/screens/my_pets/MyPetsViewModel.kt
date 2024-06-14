package com.example.buscapet.ui.screens.my_pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.data.local.Pet
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.ui.screens.last_reports.LastReportsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    petsRepository: PetsRepository
) : ViewModel() {

    data class PetUiState(
        val myPets: List<Pet> = emptyList()
    )

    val petsUiState: StateFlow<PetUiState> =
        petsRepository.getMyPets()
            .map { PetUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PetUiState()
            )

}