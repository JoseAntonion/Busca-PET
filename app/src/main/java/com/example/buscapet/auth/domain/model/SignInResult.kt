package com.example.buscapet.auth.domain.model

data class SignInResult(
    val success: Boolean = false,
    val errorMessage: String = ""
)