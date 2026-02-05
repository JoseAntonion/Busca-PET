package com.example.buscapet.add_pet.presentation

import android.net.Uri

sealed class AddPetEvent {
    data class OnImageChanged(val image: Uri) : AddPetEvent()
    data class OnNameChanged(val name: String) : AddPetEvent()
    data class OnBreedChanged(val breed: String) : AddPetEvent()
    data class OnBirthDateChanged(val birthDate: Long) : AddPetEvent()
    data class OnCheckupPlanChanged(val plan: Int) : AddPetEvent()
    data object Submit : AddPetEvent()
}