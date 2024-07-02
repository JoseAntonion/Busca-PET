package com.example.buscapet.data.local

import com.example.buscapet.domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    suspend fun insertPet(pet: Pet): Long
    fun getAllPets(): Flow<List<Pet>>

    suspend fun getPetsByOwner(owner: String): List<Pet>

    fun getPetById(id: Int): Pet

    suspend fun deleteAllPets(allPets: List<Pet>)
    suspend fun getPetsByReporter(reporter: String): List<Pet>
}