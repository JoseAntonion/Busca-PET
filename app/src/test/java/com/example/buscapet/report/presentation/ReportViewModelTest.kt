package com.example.buscapet.report.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.buscapet.add_pet.domain.use_case.AnalyzePetImageUseCase
import com.example.buscapet.core.data.util.BitmapUtils
import com.example.buscapet.core.domain.classification.ClassificationResult
import com.example.buscapet.data.local.PetsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReportViewModelTest {

    private lateinit var viewModel: ReportViewModel
    private val petsRepository: PetsRepository = mockk(relaxed = true)
    private val analyzePetImage: AnalyzePetImageUseCase = mockk()
    private val bitmapUtils: BitmapUtils = mockk()
    private val application: Application = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ReportViewModel(
            petsRepository,
            application,
            savedStateHandle,
            analyzePetImage,
            bitmapUtils
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent OnImageChanged triggers analysis and successful report`() = runTest {
        // GIVEN
        val fakeUri = mockk<Uri>()
        val fakeBitmap = mockk<android.graphics.Bitmap>()
        val fakeClassificationResult = ClassificationResult("Perro", 0.9f)

        coEvery { bitmapUtils.getBitmapFromUri(any()) } returns fakeBitmap
        coEvery { analyzePetImage(fakeBitmap, any()) } returns listOf(fakeClassificationResult)
        coEvery { petsRepository.insertPet(any()) } returns true

        viewModel.uiEvent.test {
            // WHEN
            viewModel.onEvent(ReportEvent.OnImageChanged(fakeUri))

            // THEN
            val successEvent = awaitItem()
            assertTrue(successEvent is ReportViewModel.ReportUiEvent.SuccessNavigate)

            // Verify state
            assertEquals(fakeUri.toString(), viewModel.formState.petImage)

            cancelAndConsumeRemainingEvents()
        }
    }
}