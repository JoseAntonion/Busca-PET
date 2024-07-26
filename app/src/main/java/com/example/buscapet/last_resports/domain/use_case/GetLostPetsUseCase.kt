package com.example.buscapet.last_resports.domain.use_case

import com.example.buscapet.data.local.PetsRepository
import javax.inject.Inject

class GetLostPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    // Use "operator fun invoke()" instead of "fun execute()" to cal this use case as a function
    // Ej. getLostPetsUseCase(params) instead of getLostPetsUseCase.getLostPets()
    operator fun invoke() = petsRepository.getLostPets()


}