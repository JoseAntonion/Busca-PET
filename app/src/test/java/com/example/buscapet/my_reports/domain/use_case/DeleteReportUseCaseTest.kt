package com.example.buscapet.my_reports.domain.use_case

import com.example.buscapet.data.local.PetsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeleteReportUseCaseTest {

    private val petsRepository: PetsRepository = mockk(relaxed = true)
    private val deleteReportUseCase = DeleteReportUseCase(petsRepository)

    @Test
    fun `when invoke is called, then repository deletePet is called`() = runBlocking {
        val petId = "pet123"
        coEvery { petsRepository.deletePet(petId) } returns true

        val result = deleteReportUseCase(petId)

        coVerify { petsRepository.deletePet(petId) }
        assertTrue(result)
    }

    @Test
    fun `when repository fails, returns false`() = runBlocking {
        val petId = "pet123"
        coEvery { petsRepository.deletePet(petId) } returns false

        val result = deleteReportUseCase(petId)

        assertFalse(result)
    }
}