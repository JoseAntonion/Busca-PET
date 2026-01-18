package com.example.buscapet.auth.domain.repository

interface AuthRepository {
    suspend fun logout()
}