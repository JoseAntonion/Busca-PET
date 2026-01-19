package com.example.buscapet.auth.data.di

import com.example.buscapet.auth.domain.use_case.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class SignInModule {
    @Provides
    fun provideSignInUseCase(): SignInUseCase {
        return SignInUseCase()
    }
}