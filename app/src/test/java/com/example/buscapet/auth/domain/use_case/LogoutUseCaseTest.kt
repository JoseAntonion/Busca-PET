package com.example.buscapet.auth.domain.use_case

import com.example.buscapet.auth.domain.repository.AuthRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LogoutUseCaseTest {

    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val logoutUseCase = LogoutUseCase(authRepository)

    @Test
    fun `when invoke is called, then repository logout is called`() = runBlocking {
        logoutUseCase()
        coVerify { authRepository.logout() }
    }
}