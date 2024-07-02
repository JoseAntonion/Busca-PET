package com.example.buscapet.ui.screens.my_reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.domain.model.Pet
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyReportsViewModel @Inject constructor(
    petsRepository: PetsRepository
) : ViewModel() {

    companion object {
        private const val MILLIS = 5_000L
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val currentUser = FirebaseAuth.getInstance().currentUser?.displayName

    data class UiState(
        val myPets: List<Pet> = emptyList()
    )

    val petsUiState: StateFlow<UiState> =
        petsRepository.getPetsByReporter(currentUser ?: "")
            .map { UiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(MILLIS),
                initialValue = UiState()
            )

}