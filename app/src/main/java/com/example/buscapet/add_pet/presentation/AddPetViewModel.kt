package com.example.buscapet.add_pet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petRepository: PetsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val inputEnable: Boolean = true,
        var isValid: Boolean = true,
        val inserted: Boolean = false
    )

    private fun toggleLoadingState() {
        _state.update { state -> state.copy(loading = !state.loading) }
    }

    private fun dataAreInserted(value: Boolean) {
        _state.update { state -> state.copy(inserted = value) }
    }

    suspend fun savePet(pet: Pet) {
        toggleLoadingState()
        viewModelScope.launch {
            petRepository.insertPet(pet)
            val response = petRepository.insertPet(pet)
            if (response > 0)
                dataAreInserted(true)
        }
    }

}