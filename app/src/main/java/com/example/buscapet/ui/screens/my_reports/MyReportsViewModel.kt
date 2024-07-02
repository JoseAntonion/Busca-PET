package com.example.buscapet.ui.screens.my_reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.domain.model.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReportsViewModel @Inject constructor(
    petsRepository: PetsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val myPets: List<Pet> = emptyList()
    )

    init {
        viewModelScope.launch {
            val myPets = petsRepository.getPetsByReporter("")
            _uiState.value = UiState(myPets = myPets)
        }
    }
}