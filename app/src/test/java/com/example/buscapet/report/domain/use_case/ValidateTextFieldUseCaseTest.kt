package com.example.buscapet.report.domain.use_case

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ValidateTextFieldUseCaseTest {

    private lateinit var validateTextFieldUseCase: ValidateTextFieldUseCase

    @Before
    fun setUp() {
        validateTextFieldUseCase = ValidateTextFieldUseCase()
    }

    @Test
    fun `Null field, returns false`() {
        val result = validateTextFieldUseCase.invoke(null)
        assertEquals(result.isValid, false)
    }

}