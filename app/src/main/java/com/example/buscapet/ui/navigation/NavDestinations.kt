package com.example.buscapet.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

@Serializable
object LastReports

@Serializable
object MyReports

@Serializable
data class DetailReport(
    val nombre: String
)
