package cl.jumpitt.roomcapsule.domain.use_cases

import cl.jumpitt.roomcapsule.data.CatBreedRepository
import cl.jumpitt.roomcapsule.di.DIo
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DeleteAllCatBreedUseCase @Inject constructor(
    private val repository: CatBreedRepository,
    @DIo private val coroutineContext: CoroutineContext
) {
    suspend operator fun invoke() = withContext(coroutineContext) { repository.deleteAll() }
}