package com.example.buscapet.home.presentation

import android.net.Uri
import com.example.buscapet.core.domain.model.Pet

data class HomeState(
    val isLoading: Boolean = false,
    val myPets: List<Pet> = emptyList(),
    val currentUser: String? = null,
    val photo: Uri? = null
)