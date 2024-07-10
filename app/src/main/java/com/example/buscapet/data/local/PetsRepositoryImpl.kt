package com.example.buscapet.data.local

import com.example.buscapet.domain.model.Pet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PetsRepositoryImpl @Inject constructor(
    private val petDao: PetDao
) : PetsRepository {

    override suspend fun insertPet(pet: Pet) = petDao.insertPet(pet)

    override fun getAllPets(): Flow<List<Pet>> = petDao.getAllPets()

    override suspend fun getPetsByOwner(owner: String): List<Pet> = petDao.getPetsByOwner(owner)

    override fun getPetById(id: Int): Pet = petDao.getPet(id)

    override suspend fun deleteAllPets(allPets: List<Pet>) = petDao.deleteAllPets(allPets)

    override fun getPetsByReporter(reporter: String): Flow<List<Pet>> =
        petDao.getPetsByReporter(reporter)

}