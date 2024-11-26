package com.example.buscapet.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object SignIn

@Serializable
object Home

@Serializable
object LastReports

@Serializable
object MyReports

@Serializable
object MyPets

@Serializable
object Profile

@Serializable
object AddPet

@Serializable
object Report

@Serializable
data class DetailReport(
    val id: Int
)
