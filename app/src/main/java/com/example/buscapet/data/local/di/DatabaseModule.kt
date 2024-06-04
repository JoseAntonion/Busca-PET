package com.example.buscapet.data.local.di

import android.content.Context
import androidx.room.Room
import com.example.buscapet.data.local.PetDao
import com.example.buscapet.data.local.PetsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun providePetDao(petDatabase: PetsDatabase): PetDao {
        return petDatabase.petsDao()
    }

    @Provides
    @Singleton
    fun providePetDatabase(@ApplicationContext context: Context): PetsDatabase {
        return Room.databaseBuilder(
            context = context,
            PetsDatabase::class.java,
            "pets_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}