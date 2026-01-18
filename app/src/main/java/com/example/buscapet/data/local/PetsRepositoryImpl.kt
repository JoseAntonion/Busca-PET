package com.example.buscapet.data.local

import com.example.buscapet.core.data.local.PetDao
import com.example.buscapet.core.domain.model.Pet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PetsRepositoryImpl @Inject constructor(
    private val petDao: PetDao 
) : PetsRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("mascotas")

    override suspend fun insertPet(pet: Pet): Boolean {
        return try {
            collection.document(pet.id).set(pet).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getLostPets(): Flow<List<Pet>> = callbackFlow {
        val subscription = collection
            .whereEqualTo("petState", "LOST")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                val pets = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject<Pet>()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } ?: emptyList()
                trySend(pets.sortedByDescending { it.timestamp })
            }
        awaitClose { subscription.remove() }
    }

    override fun getPetsByOwner(owner: String): Flow<List<Pet>> = callbackFlow {
        val subscription = collection
            .whereEqualTo("ownerId", owner)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                val pets = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject<Pet>()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } ?: emptyList()
                trySend(pets)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getPetById(id: String): Pet? {
        return try {
            val snapshot = collection.document(id).get().await()
            snapshot.toObject<Pet>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteAllPets(allPets: List<Pet>) {
        // Not implemented
    }

    override suspend fun deletePet(petId: String): Boolean {
        return try {
            collection.document(petId).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getPetsByReporter(reporter: String): Flow<List<Pet>> = callbackFlow {
        val subscription = collection
            .whereEqualTo("reporterId", reporter)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                val pets = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject<Pet>()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } ?: emptyList()
                trySend(pets)
            }
        awaitClose { subscription.remove() }
    }
}
