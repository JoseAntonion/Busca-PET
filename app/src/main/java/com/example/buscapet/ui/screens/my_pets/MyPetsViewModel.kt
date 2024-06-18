package com.example.buscapet.ui.screens.my_pets

import androidx.lifecycle.ViewModel
import com.example.buscapet.data.local.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    petsRepository: PetsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetUiState())
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    data class PetUiState(
        val myPets: List<Pet> = emptyList()
    )

    init {
        val myPets = petsRepository.getPetsByOwner("")
        _uiState.value = PetUiState(myPets = myPets)
    }
}