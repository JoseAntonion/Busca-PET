package com.example.buscapet.data.local

import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.Treatment
import kotlinx.coroutines.flow.Flow

interface PetsRepository {

    suspend fun insertPet(pet: Pet): Boolean

    fun getLostPets(): Flow<List<Pet>>

    fun getPetsByOwner(owner: String): Flow<List<Pet>>

    suspend fun getPetById(id: String): Pet?

    suspend fun getSimilarReports(description: String): List<Pet>

    suspend fun deleteAllPets(allPets: List<Pet>)

    suspend fun deletePet(petId: String): Boolean

    fun getPetsByReporter(reporter: String): Flow<List<Pet>>

    suspend fun getPetListByReporter(reporterId: String): List<Pet>

    fun getTreatmentsForPet(petId: String): Flow<List<Treatment>>

    suspend fun insertTreatment(treatment: Treatment)

    suspend fun updateTreatment(treatment: Treatment)

    suspend fun deleteTreatment(treatment: Treatment)

}