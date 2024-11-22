package com.example.buscapet.core.data.local.di

import com.example.buscapet.add_pet.domain.use_case.ValidatePetImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AddPetModule {
    @Provides
    fun provideValidateImageUseCase(): ValidatePetImageUseCase {
        return ValidatePetImageUseCase()
    }
}