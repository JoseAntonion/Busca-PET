package com.example.buscapet.data.local

import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    suspend fun insertPet(pet: Pet)
    fun getAllPets(): Flow<List<Pet>>
    suspend fun deleteAllPets(allPets: List<Pet>)
}