package com.example.buscapet.home.presentation

import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.data.local.PetsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val petsRepository: PetsRepository = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(petsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when reporting an already lost pet, returns error`() = runTest {
        val lostPet = Pet(id = "1", petState = PetState.LOST)

        viewModel.onReportExistingPet(lostPet)
        
        val event = viewModel.homeEvents.first()
        assertTrue(event is HomeViewModel.HomeEvent.ReportError)
    }

    @Test
    fun `when reporting a home pet, calls insertPet and returns success`() = runTest {
        val homePet = Pet(id = "2", petState = PetState.HOME)
        coEvery { petsRepository.insertPet(any()) } returns true

        viewModel.onReportExistingPet(homePet)

        coVerify { petsRepository.insertPet(any()) }
        val event = viewModel.homeEvents.first()
        assertTrue(event is HomeViewModel.HomeEvent.ReportSuccess)
    }
}