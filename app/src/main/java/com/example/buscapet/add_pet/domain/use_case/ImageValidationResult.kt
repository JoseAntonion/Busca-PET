package com.example.buscapet.add_pet.domain.use_case

data class ImageValidationResult(
    val isValid: Boolean = false,
    val error: String? = null
)