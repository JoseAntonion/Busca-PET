package com.example.buscapet.data.local

import com.example.buscapet.core.domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetsRepository {

    suspend fun insertPet(pet: Pet): Long

    fun getLostPets(): Flow<List<Pet>>

    fun getPetsByOwner(owner: String): Flow<List<Pet>>

    fun getPetById(id: Int): Pet

    suspend fun deleteAllPets(allPets: List<Pet>)

    fun getPetsByReporter(reporter: String): Flow<List<Pet>>

}