package com.example.buscapet.my_pets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPetsViewModel @Inject constructor(
    petsRepository: PetsRepository
) : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser?.displayName

    data class UiState(
        val petList: List<Pet> = emptyList()
    )

    val lostPets: StateFlow<UiState> =
        petsRepository.getPetsByOwner(currentUser ?: "")
            .map { UiState(petList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState()
            )

}