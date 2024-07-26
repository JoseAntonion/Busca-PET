package com.example.buscapet.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.buscapet.core.domain.model.Pet

@Database(entities = [Pet::class], version = 9, exportSchema = false)
abstract class PetsDatabase : RoomDatabase() {
    abstract fun petsDao(): PetDao
}