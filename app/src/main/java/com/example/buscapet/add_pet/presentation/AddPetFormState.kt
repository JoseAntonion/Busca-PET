package com.example.buscapet.add_pet.presentation

data class AddPetFormState(
    val addPetImage: String? = null,
    val addPetImageError: String? = null,
    val addPetName: String? = null,
    val addPetNameError: String? = null,
    val addPetBreed: String? = null,
    val addPetBreedError: String? = null,
    val addPetBirthDate: Long? = null,
    val addPetBirthDateFormatted: String? = null,
    val addPetBirthDateError: String? = null,
    val addPetCheckupPlan: Int? = null,
    val addPetCheckupPlanError: String? = null,
)