package com.example.buscapet.core.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "treatment",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id"],
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Treatment(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "pet_id", index = true)
    val petId: String,
    @ColumnInfo(name = "medication_name")
    val medicationName: String,
    @ColumnInfo(name = "days")
    val days: Int,
    @ColumnInfo(name = "frequency_hours")
    val frequencyHours: Int,
    @ColumnInfo(name = "start_date")
    val startDate: Long = System.currentTimeMillis()
)
