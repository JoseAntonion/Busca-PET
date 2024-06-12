package com.example.buscapet.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.data.local.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val petsRepository: PetsRepository
) : ViewModel() {

    suspend fun savePet(pet: Pet) {
        viewModelScope.launch {
            petsRepository.insertPet(pet)
        }
    }

}