package com.example.buscapet.my_reports.domain.use_case

import com.example.buscapet.data.local.PetsRepository
import javax.inject.Inject

class DeleteReportUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(petId: String): Boolean {
        return petsRepository.deletePet(petId)
    }
}