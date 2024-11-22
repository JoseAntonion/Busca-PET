package com.example.buscapet.add_pet.presentation

import android.net.Uri

sealed class AddPetEvent {
    data class OnImageChanged(val image: Uri) : AddPetEvent()
    data class OnNameChanged(val name: String) : AddPetEvent()
    data class OnBreedChanged(val breed: String) : AddPetEvent()
    data class OnAgeChanged(val age: String) : AddPetEvent()
    data class OnBirthChanged(val birth: String) : AddPetEvent()
    data object Submit : AddPetEvent()
}