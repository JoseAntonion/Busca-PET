package com.example.buscapet.profile.presentation

import android.net.Uri

data class ProfileState(
    val loading: Boolean = false,
    val name: String? = null,
    val email: String? = null,
    val photo: Uri? = null
)