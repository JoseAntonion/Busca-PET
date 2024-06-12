package com.example.buscapet.data.local

import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    suspend fun insertPet(pet: Pet)
    fun getAllPets(): Flow<List<Pet>>

    fun getPetById(id: Int): Pet

    suspend fun deleteAllPets(allPets: List<Pet>)
}