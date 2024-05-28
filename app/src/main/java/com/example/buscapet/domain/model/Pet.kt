package com.example.buscapet.domain.model

import android.net.Uri

class Pet(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val raza: String,
    val foto: Uri?,
    val cover: String? = null
)
