package cl.jumpitt.roomcapsule.domain.use_cases

import androidx.lifecycle.LiveData
import cl.jumpitt.roomcapsule.data.CatBreedRepository
import cl.jumpitt.roomcapsule.di.DIo
import cl.jumpitt.roomcapsule.domain.model.CatBreed
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetAllCatBreedUseCase @Inject constructor(
    private val repository: CatBreedRepository,
    @DIo private val coroutineContext: CoroutineContext
) {
    suspend operator fun invoke(): LiveData<List<CatBreed>> = withContext(coroutineContext) { repository.getAll() }
}