package cl.jumpitt.roomcapsule.domain.use_cases

import cl.jumpitt.roomcapsule.data.CatBreedRemoteRepository
import cl.jumpitt.roomcapsule.di.DIo
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FetchCatBreedUseCase @Inject constructor(
    private val repository: CatBreedRemoteRepository,
    private val insertAllCatBreedUseCase: InsertAllCatBreedUseCase,
    @DIo private val coroutineContext: CoroutineContext
) {
    suspend operator fun invoke(page: Int) = withContext(coroutineContext) {
        repository.fetchCatBreeds(page).apply {
            if (isNotEmpty()) {
                insertAllCatBreedUseCase(this)
            }
        }
    }
}