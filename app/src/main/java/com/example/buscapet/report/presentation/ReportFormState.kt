package com.example.buscapet.report.presentation

data class ReportFormState(
    val petId: String? = null,
    val petImage: String? = null,
    val petImageError: String? = null,
    val name: String = "",
    val description: String = "",
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null
)