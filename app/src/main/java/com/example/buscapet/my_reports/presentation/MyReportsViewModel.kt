package com.example.buscapet.my_reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.my_reports.domain.use_case.GetMyReportsUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReportsViewModel @Inject constructor(
    private val getMyReportsUseCase: GetMyReportsUseCase
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userId = currentUser?.uid ?: ""

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMyReports()
    }

    fun refresh() {
        loadMyReports(isRefresh = true)
    }

    private fun loadMyReports(isRefresh: Boolean = false) {
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

            getMyReportsUseCase(userId)
                .catch {
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
                }
                .collect { pets ->
                    _uiState.update {
                        it.copy(
                            myPets = pets,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
        }
    }

    data class UiState(
        val myPets: List<Pet> = emptyList(),
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false
    )
}