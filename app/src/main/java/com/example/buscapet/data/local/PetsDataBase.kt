package com.example.buscapet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pet::class], version = 3, exportSchema = false)
abstract class PetsDatabase : RoomDatabase() {
    abstract fun petsDao(): PetDao
}