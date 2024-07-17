package com.example.buscapet.core.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String? = null,
    @ColumnInfo(name = "breed")
    val breed: String? = null,
    @ColumnInfo(name = "age")
    val age: String? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "owner")
    val owner: String? = null,
    @ColumnInfo(name = "reporter")
    val reporter: String? = null,
    @ColumnInfo(name = "pet_state")
    val petState: String? = null
)