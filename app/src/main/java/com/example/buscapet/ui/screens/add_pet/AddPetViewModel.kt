package com.example.buscapet.ui.screens.add_pet

import androidx.lifecycle.ViewModel
import com.example.buscapet.data.local.PetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    petRepository: PetsRepository
): ViewModel() {


}