package com.example.buscapet.report.presentation

data class ReportPetFormState(
    var petName: String? = null,
    val petNameError: String? = null,
    val petAge: String? = null,
    val petAgeError: String? = null,
    val petBreed: String? = null,
    val petBreedError: String? = null,
    val petDescription: String? = null,
    val petDescriptionError: String? = null,
)
