package com.example.buscapet.core.data.local.di

import com.example.buscapet.data.local.PetsRepository
import com.example.buscapet.data.local.PetsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPetRepository(impl: PetsRepositoryImpl): PetsRepository
}