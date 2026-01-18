package com.example.buscapet.my_reports.domain.use_case

import com.example.buscapet.data.local.PetsRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GetMyReportsUseCaseTest {

    private val petsRepository: PetsRepository = mockk(relaxed = true)
    private val getMyReportsUseCase = GetMyReportsUseCase(petsRepository)

    @Test
    fun `when invoke is called, then getPetsByReporter is called with correct userId`() {
        val userId = "user123"
        getMyReportsUseCase(userId)
        verify { petsRepository.getPetsByReporter(userId) }
    }
}