package com.example.buscapet.my_pets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userId = currentUser?.uid ?: ""

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMyPets()
    }

    fun refresh() {
        loadMyPets(isRefresh = true)
    }

    private fun loadMyPets(isRefresh: Boolean = false) {
        if (userId.isEmpty()) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true) }
            } else {
                _uiState.update { it.copy(isLoading = true) }
            }

            petsRepository.getPetsByOwner(userId)
                .catch {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                }
                .collect { pets ->
                    _uiState.update {
                        it.copy(
                            petList = pets,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
        }
    }

    data class UiState(
        val petList: List<Pet> = emptyList(),
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false
    )
}