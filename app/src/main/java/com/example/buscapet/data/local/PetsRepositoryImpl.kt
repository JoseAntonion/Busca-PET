package com.example.buscapet.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PetsRepositoryImpl @Inject constructor(
    private val petDao: PetDao
) : PetsRepository {
    override suspend fun insertPet(pet: Pet) = petDao.insertPet(pet)

    override fun getAllPets(): Flow<List<Pet>> = petDao.getAllPets()

    override suspend fun deleteAllPets(allPets: List<Pet>) = petDao.deleteAllPets(allPets)

}