package com.example.iotapp.data.model

data class LampState(
    val id: String,
    val name: String,
    val on: Boolean,
    val brightness: Int,
    val colorName: String,
    val xy: List<Double>? = null
)
