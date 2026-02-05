package com.example.buscapet.core.domain.use_case

import android.location.Location
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.data.local.PetsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CheckProximityUseCaseTest {

    private lateinit var useCase: CheckProximityUseCase
    private val petsRepository: PetsRepository = mockk()

    @Before
    fun setUp() {
        useCase = CheckProximityUseCase(petsRepository)
    }

    private fun createMockLocation(lat: Double, lon: Double): Location {
        val mockLocation = mockk<Location>(relaxed = true)
        coEvery { mockLocation.latitude } returns lat
        coEvery { mockLocation.longitude } returns lon
        return mockLocation
    }

    @Test
    fun `invoke returns pet type when user is within proximity`() = runTest {
        // GIVEN
        val userLocation = createMockLocation(10.0, 10.0)
        val petReport = Pet(
            id = "1",
            description = "Perro",
            latitude = 10.0005, // ~55 meters away
            longitude = 10.0
        )
        coEvery { petsRepository.getPetListByReporter(any()) } returns listOf(petReport)

        // WHEN
        val result = useCase(userLocation)

        // THEN
        assertNotNull(result)
        assertEquals("Perro", result)
    }

    @Test
    fun `invoke returns null when user is outside proximity`() = runTest {
        // GIVEN
        val userLocation = createMockLocation(10.0, 10.0)
        val petReport = Pet(
            id = "1",
            description = "Gato",
            latitude = 10.01, // ~1.1 km away
            longitude = 10.0
        )
        coEvery { petsRepository.getPetListByReporter(any()) } returns listOf(petReport)

        // WHEN
        val result = useCase(userLocation)

        // THEN
        assertNull(result)
    }

    @Test
    fun `invoke returns null for the same route if notified recently`() = runTest {
        // GIVEN
        val userLocation = createMockLocation(10.0, 10.0)
        val petReport = Pet(
            id = "1",
            description = "Perro",
            latitude = 10.0005,
            longitude = 10.0
        )
        coEvery { petsRepository.getPetListByReporter(any()) } returns listOf(petReport)

        // WHEN
        val firstResult = useCase(userLocation)
        val secondResult = useCase(userLocation)

        // THEN
        assertNotNull(firstResult)
        assertEquals("Perro", firstResult)
        assertNull("Should be null due to debounce", secondResult)
    }
}