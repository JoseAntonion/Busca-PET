package com.example.buscapet.my_reports.domain.use_case

import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyReportsUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    operator fun invoke(userId: String): Flow<List<Pet>> {
        return petsRepository.getPetsByReporter(userId)
    }
}