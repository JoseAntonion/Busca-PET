package com.example.buscapet.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object SignIn

@Serializable
object Home

@Serializable
object Profile

@Serializable
object AddPet

@Serializable
data class Report(
    val petId: String? = null
)

@Serializable
data class DetailReport(
    val id: String
)

@Serializable
data class MedicalTreatment(
    val petId: String
)

@Serializable
data class ReportMap(
    val latitude: String,
    val longitude: String,
    val petName: String
)