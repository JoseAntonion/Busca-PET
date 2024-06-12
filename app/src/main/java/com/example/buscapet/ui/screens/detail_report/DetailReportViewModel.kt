package com.example.buscapet.ui.screens.detail_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscapet.data.local.Pet
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailReportViewModel @Inject constructor(
    petRepository: PetsRepository
) : ViewModel() {

    private val _petDetails: MutableStateFlow<Pet?> = MutableStateFlow(null)
    val petDetails: StateFlow<Pet?> = _petDetails

    private val _petId: MutableStateFlow<Int> = MutableStateFlow(0)
    //val petId: StateFlow<Int> = _petId

    init {
        viewModelScope.launch {
            val pet: Pet
            withContext(Dispatchers.IO) {
                pet = petRepository.getPetById(_petId.value)
            }
            withContext(Dispatchers.Main) {
                _petDetails.value = pet
            }
        }
    }

    fun setPetId(petId: Int) {
        _petId.value = petId
    }

}