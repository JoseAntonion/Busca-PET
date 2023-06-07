package com.example.buscapet.data

import com.example.buscapet.data.network.API
import com.example.buscapet.di.NormalApi
import javax.inject.Inject

class CatBreedRemoteRepository @Inject constructor(
    @NormalApi private val api: API
) {
    //suspend fun fetchCatBreeds(page: Int): List<CatBreedModel> {
    //    return api.fetchBreeds(page).data
    //}
}