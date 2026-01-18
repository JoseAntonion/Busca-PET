package com.example.buscapet.core.data.local

import androidx.room.TypeConverter
import com.example.buscapet.core.domain.model.Caracteristic
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromCaracteristicList(caracteristics: List<Caracteristic>?): String {
        val gson = Gson()
        return gson.toJson(caracteristics)
    }

    @TypeConverter
    fun toCaracteristicList(data: String?): List<Caracteristic> {
        val listType = object : TypeToken<List<Caracteristic>>() {}.type
        return Gson().fromJson(data, listType)
    }
}