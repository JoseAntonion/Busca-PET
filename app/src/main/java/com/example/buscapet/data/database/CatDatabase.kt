package com.example.buscapet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.buscapet.data.database.entity.CatBreedEntity

@Database(entities = [CatBreedEntity::class], version = 1)
//@TypeConverters(Converters::class)
abstract class CatDatabase : RoomDatabase() {
    //abstract fun getCatBreedDao(): CatBreedDao
}