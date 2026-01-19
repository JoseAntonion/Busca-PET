package com.example.buscapet.my_reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.my_reports.domain.use_case.GetMyReportsUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyReportsViewModel @Inject constructor(
    getMyReportsUseCase: GetMyReportsUseCase
) : ViewModel() {

    companion object {
        private const val MILLIS = 5_000L
    }

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userId = currentUser?.uid ?: ""

    data class UiState(
        val myPets: List<Pet> = emptyList()
    )

    val petsUiState: StateFlow<UiState> =
        getMyReportsUseCase(userId)
            .map { UiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(MILLIS),
                initialValue = UiState()
            )

}