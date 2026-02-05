package com.example.buscapet.detail_report.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.my_reports.domain.use_case.DeleteReportUseCase
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailReportViewModelTest {

    private lateinit var viewModel: DetailReportViewModel
    private val petRepository: PetsRepository = mockk()
    private val deleteReportUseCase: DeleteReportUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init fetches pet details and similar reports to create route`() = runTest {
        // GIVEN
        val petId = "123"
        val petDescription = "Perro"
        val mainPet =
            Pet(id = petId, description = petDescription, latitude = 10.0, longitude = 10.0)
        val similarPet1 =
            Pet(id = "124", description = petDescription, latitude = 11.0, longitude = 11.0)
        val similarPet2 =
            Pet(id = "125", description = petDescription, latitude = 12.0, longitude = 12.0)

        val savedStateHandle = SavedStateHandle(mapOf("id" to petId))
        coEvery { petRepository.getPetById(petId) } returns mainPet
        coEvery { petRepository.getSimilarReports(petDescription) } returns listOf(
            mainPet,
            similarPet1,
            similarPet2
        )
        coEvery { petRepository.getTreatmentsForPet(any()) } returns flowOf(emptyList())

        // WHEN
        viewModel = DetailReportViewModel(petRepository, deleteReportUseCase, savedStateHandle)

        // Advance timers to allow coroutines to execute
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        assertEquals(mainPet, viewModel.petDetails.value)

        val expectedCoordinates = listOf(
            LatLng(10.0, 10.0),
            LatLng(11.0, 11.0),
            LatLng(12.0, 12.0)
        )
        assertEquals(expectedCoordinates, viewModel.routeCoordinates.value)
    }
}