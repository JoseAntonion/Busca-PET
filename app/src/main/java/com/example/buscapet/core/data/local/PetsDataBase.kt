package com.example.buscapet.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.buscapet.core.domain.model.Pet

@Database(entities = [Pet::class], version = 11, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PetsDatabase : RoomDatabase() {
    abstract fun petsDao(): PetDao
}