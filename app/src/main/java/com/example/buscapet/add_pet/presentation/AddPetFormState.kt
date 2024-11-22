package com.example.buscapet.add_pet.presentation

data class AddPetFormState(
    val addPetImage: String? = null,
    val addPetImageError: String? = null,
    val addPetName: String? = null,
    val addPetNameError: String? = null,
    val addPetBreed: String? = null,
    val addPetBreedError: String? = null,
    val addPetAge: String? = null,
    val addPetAgeError: String? = null,
    val addPetBirth: String? = null,
    val addPetBirthError: String? = null,
)