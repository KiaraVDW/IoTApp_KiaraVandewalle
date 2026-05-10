package com.example.iotapp.data.api

import com.example.iotapp.data.model.HueLight
import com.example.iotapp.data.model.HueLightUpdate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Url

interface HueApi {
    @GET
    suspend fun getLights(@Url url: String): Map<String, HueLight>

    @PUT
    suspend fun updateLightState(
        @Url url: String,
        @Body update: HueLightUpdate
    ): List<Map<String, Any>>
}
