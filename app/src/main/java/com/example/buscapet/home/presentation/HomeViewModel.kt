package com.example.buscapet.home.presentation

import androidx.lifecycle.ViewModel
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val reportDialog: Boolean = false,
        val loading: Boolean = false,
        val myPets: List<Pet> = emptyList(),
        val currentUser: FirebaseUser? = null
    )

    init {
        _uiState.update { it.copy(currentUser = currentUser) }
    }

    fun showReportDialog() {
        _uiState.value = UiState(reportDialog = true)
    }

    fun dismissReportDialog() {
        _uiState.value = UiState(reportDialog = false)
    }

}