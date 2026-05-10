package com.example.iotapp.data.model

import com.google.gson.annotations.SerializedName

data class HueLight(
    val name: String = "Lamp",
    val state: HueLightState = HueLightState()
)

data class HueLightState(
    val on: Boolean = false,
    @SerializedName("bri") val brightness: Int = 100,
    @SerializedName("xy") val xy: List<Double>? = null
)

data class HueLightUpdate(
    val on: Boolean? = null,
    @SerializedName("bri") val brightness: Int? = null,
    @SerializedName("xy") val xy: List<Double>? = null
)

data class ConnectionSettings(
    val ipAddress: String = "",
    val username: String = ""
)
