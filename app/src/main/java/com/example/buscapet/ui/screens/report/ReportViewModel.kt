package com.example.buscapet.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val petsRepository: PetsRepository
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

    fun toggleInputEnableState() {
        _state.update { state -> state.copy(inputEnable = !state.inputEnable) }
    }

    private fun dataAreInserted(value: Boolean) {
        _state.update { state -> state.copy(inserted = value) }
    }

    suspend fun savePet(pet: Pet) {
        toggleLoadingState()
        toggleInputEnableState()
        viewModelScope.launch {
            val response = petsRepository.insertPet(pet)
            if (response > 0)
                dataAreInserted(true)
        }
    }

}