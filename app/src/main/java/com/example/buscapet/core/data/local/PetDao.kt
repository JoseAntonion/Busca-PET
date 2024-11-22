package com.example.buscapet.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet WHERE pet_state = :perdido OR pet_state = :aSalvoPerdido")
    fun getLostPets(
        perdido: PetState = PetState.LOST,
        aSalvoPerdido: PetState = PetState.SAFE_LOST
    ): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE id = :id")
    fun getPet(id: Int): Pet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(petsData: Pet): Long

    @Delete
    suspend fun deleteAllPets(allPets: List<Pet>)

    @Query("SELECT * FROM pet WHERE owner = :owner")
    fun getPetsByOwner(owner: String): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE reporter = :reporter")
    fun getPetsByReporter(reporter: String): Flow<List<Pet>>
}