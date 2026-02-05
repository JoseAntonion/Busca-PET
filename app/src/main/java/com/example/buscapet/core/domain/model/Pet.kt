package com.example.buscapet.core.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    val name: String? = null,
    @ColumnInfo(name = "breed")
    val breed: String? = null,
    @ColumnInfo(name = "birth_date")
    val birthDate: Long? = null,
    @ColumnInfo(name = "checkup_plan")
    val checkupPlan: Int? = null,
    @ColumnInfo(name = "last_checkup_date")
    val lastCheckupDate: Long? = null,
    @ColumnInfo(name = "caracteristics")
    val caracteristics: List<Caracteristic> = emptyList(),
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "pet_state")
    val petState: PetState? = null,
    @ColumnInfo(name = "last_seen")
    val lastSeen: String? = null,
    @ColumnInfo(name = "image")
    val image: String? = null,
    @ColumnInfo(name = "owner_id")
    val ownerId: String? = null,
    @ColumnInfo(name = "reporter_id")
    val reporterId: String? = null,
    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,
    @ColumnInfo(name = "longitude")
    val longitude: Double? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
