package com.example.buscapet.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val reportDialog: Boolean = false,
        val loading: Boolean = false,
        val myPets: List<Pet> = emptyList()
    )

    fun showReportDialog() {
        _uiState.value = UiState(reportDialog = true)
    }

    fun dismissReportDialog() {
        _uiState.value = UiState(reportDialog = false)
    }

    fun onLostMyOwnClick(owner: String?) {
        viewModelScope.launch {
            val myPets = petsRepository.getPetsByOwner(owner ?: "")
            _uiState.value = UiState(myPets = myPets)
        }
    }

}