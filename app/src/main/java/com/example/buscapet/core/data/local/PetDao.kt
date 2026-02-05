package com.example.buscapet.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.domain.model.Treatment
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet WHERE pet_state = :perdido OR pet_state = :aSalvoPerdido")
    fun getLostPets(
        perdido: PetState = PetState.LOST,
        aSalvoPerdido: PetState = PetState.SAFE_LOST
    ): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE id = :id")
    fun getPet(id: String): Pet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(petsData: Pet): Long

    @Delete
    suspend fun deleteAllPets(allPets: List<Pet>)

    @Query("SELECT * FROM pet WHERE owner_id = :ownerId")
    fun getPetsByOwner(ownerId: String): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE description = :reporter")
    fun getPetsByReporter(reporter: String): Flow<List<Pet>>

    // Treatment queries
    @Query("SELECT * FROM treatment WHERE pet_id = :petId")
    fun getTreatmentsForPet(petId: String): Flow<List<Treatment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreatment(treatment: Treatment)

    @Update
    suspend fun updateTreatment(treatment: Treatment)

    @Delete
    suspend fun deleteTreatment(treatment: Treatment)
}
