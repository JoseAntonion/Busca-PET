package com.example.buscapet.core.data.local.di

import android.content.Context
import androidx.room.Room
import com.example.buscapet.core.data.local.PetsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    private const val DATABASE_NAME = "pets_database"

    @Provides
    fun providePetDao(petDatabase: PetsDatabase) = petDatabase.petsDao()

    @Provides
    @Singleton
    fun providePetDatabase(@ApplicationContext context: Context): PetsDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = PetsDatabase::class.java,
            name = DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}