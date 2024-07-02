package com.example.buscapet.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

@Serializable
object LastReports

@Serializable
object MyPets

@Serializable
object Report

@Serializable
data class DetailReport(
    val id: Int
)
