package com.example.buscapet.domain.model

import java.util.Date

data class CatBreed(
    val breed: String,
    val country: String,
    val origin: String,
    val coat: String,
    val pattern: String,
    val date: Date,
    var id: Long?
)

//fun CatBreedEntity.toDomain() = CatBreed(breed, country, origin, coat, pattern, date, id)