package com.example.buscapet.data.local

import com.example.buscapet.core.data.local.PetDao
import com.example.buscapet.core.domain.model.Pet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PetsRepositoryImpl @Inject constructor(
    private val petDao: PetDao
) : PetsRepository {

    override suspend fun insertPet(pet: Pet) = petDao.insertPet(pet)

    override fun getLostPets(): Flow<List<Pet>> = petDao.getLostPets()

    override fun getPetsByOwner(owner: String): Flow<List<Pet>> = petDao.getPetsByOwner(owner)

    override fun getPetById(id: Int): Pet = petDao.getPet(id)

    override suspend fun deleteAllPets(allPets: List<Pet>) = petDao.deleteAllPets(allPets)

    override fun getPetsByReporter(reporter: String): Flow<List<Pet>> =
        petDao.getPetsByReporter(reporter)

}