package com.example.buscapet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.buscapet.domain.model.Pet

@Database(entities = [Pet::class], version = 6, exportSchema = false)
abstract class PetsDatabase : RoomDatabase() {
    abstract fun petsDao(): PetDao
}