package com.example.buscapet.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buscapet.core.domain.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE id = :id")
    fun getPet(id: Int): Pet

    //@Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(petsData: Pet): Long

    @Delete
    suspend fun deleteAllPets(allPets: List<Pet>)

    @Query("SELECT * FROM pet WHERE owner = :owner")
    suspend fun getPetsByOwner(owner: String): List<Pet>

    @Query("SELECT * FROM pet WHERE reporter = :reporter")
    fun getPetsByReporter(reporter: String): Flow<List<Pet>>
}