package com.example.buscapet.last_resports.domain.use_case

import com.example.buscapet.data.local.PetsRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GetLostPetsUseCaseTest {

    private val petsRepository: PetsRepository = mockk(relaxed = true)
    private val getLostPetsUseCase = GetLostPetsUseCase(petsRepository)

    @Test
    fun `when invoke is called, then getLostPets is called`() {
        getLostPetsUseCase()
        verify { petsRepository.getLostPets() }
    }
}