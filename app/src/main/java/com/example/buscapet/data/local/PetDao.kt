package com.example.buscapet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE id = :id")
    fun getPet(id: Int): Pet

    @Insert
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(petsData: Pet): Long

    @Delete
    suspend fun deleteAllPets(allPets: List<Pet>)
}