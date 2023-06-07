package com.example.buscapet.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


/*
{
    "data": [
        {
            "breed": "Abyssinian",
            "country": "Ethiopia",
            "origin": "Natural/Standard",
            "coat": "Short",
            "pattern": "Ticked"
        }
    ]
}
 */

@Entity(tableName = "breed")
data class CatBreedEntity(
    @ColumnInfo(name = "breed") val breed: String,
    val country: String,
    val origin: String,
    val coat: String,
    val pattern: String,
    val date: Date,
    @PrimaryKey(autoGenerate = true) var id: Long? = null
)
