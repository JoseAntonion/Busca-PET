package com.example.buscapet.home.presentation

import android.net.Uri
import com.example.buscapet.core.domain.model.Pet
import com.google.firebase.auth.FirebaseUser

data class HomeState(
    val loading: Boolean = false,
    val myPets: List<Pet> = emptyList(),
    val currentUser: String? = null,
    val photo: Uri? = null
)
