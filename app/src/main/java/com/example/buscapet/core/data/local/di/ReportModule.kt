package com.example.buscapet.core.data.local.di

import com.example.buscapet.report.domain.use_case.ValidateTextFieldUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ReportModule {

    @Provides
    fun provideValidateTextFieldUseCase(): ValidateTextFieldUseCase {
        return ValidateTextFieldUseCase()
    }

}