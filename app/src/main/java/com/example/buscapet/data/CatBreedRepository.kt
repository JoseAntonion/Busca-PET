package cl.jumpitt.roomcapsule.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import cl.jumpitt.roomcapsule.data.database.dao.CatBreedDao
import cl.jumpitt.roomcapsule.data.database.entity.toDatabase
import cl.jumpitt.roomcapsule.data.network.model.CatBreedModel
import cl.jumpitt.roomcapsule.data.network.model.toDatabase
import cl.jumpitt.roomcapsule.domain.model.CatBreed
import cl.jumpitt.roomcapsule.domain.model.toDomain
import javax.inject.Inject

class CatBreedRepository @Inject constructor(
    private val dao: CatBreedDao
) {
    fun getAll(): LiveData<List<CatBreed>> {
        val response = dao.getAll()
        return Transformations.map(response) { result ->
            result.map { it.toDomain() }
        }
    }

    suspend fun insertAll(list: List<CatBreedModel>) {
        dao.insertAll(list.map { it.toDatabase() })
    }

    suspend fun insert(catBreed: CatBreed) {
        dao.insert(catBreed.toDatabase())
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}