package com.example.buscapet.last_resports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.last_resports.domain.use_case.GetLostPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LastReportsViewModel @Inject constructor(
    private val getLostPetsUseCase: GetLostPetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetUiState())
    val petsUiState = _uiState.asStateFlow()

    init {
        loadPets()
    }

    fun refresh() {
        loadPets(isRefresh = true)
    }

    private fun loadPets(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true) }
            } else {
                _uiState.update { it.copy(isLoading = true) }
            }

            getLostPetsUseCase()
                .catch {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                }
                .collect { pets ->
                    _uiState.update {
                        it.copy(
                            pets = pets,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
        }
    }

    data class PetUiState(
        val pets: List<Pet> = emptyList(),
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false
    )
}