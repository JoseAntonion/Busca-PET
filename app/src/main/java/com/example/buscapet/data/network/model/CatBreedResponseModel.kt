package com.example.buscapet.data.network.model

import com.google.gson.annotations.SerializedName


data class CatBreedResponseModel(
    @SerializedName("data") val data: List<CatBreedModel>
)
