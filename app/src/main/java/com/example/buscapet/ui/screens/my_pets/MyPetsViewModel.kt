package com.example.buscapet.ui.screens.my_pets

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
class MyPetsViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {



    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _currentUserName: MutableStateFlow<String> = MutableStateFlow("")

    data class UiState(
        val petList: List<Pet> = emptyList()
    )

    init {
        viewModelScope.launch {
            val myPets = petsRepository.getPetsByOwner(_currentUserName.value)
            _uiState.value = UiState(petList = myPets)
        }
    }

    fun setCurrentUserName(name: String?) {
        _currentUserName.value = name ?: ""
    }

}