package com.example.kotlinpet.model

data class CurrentWeather(
    val temperatureC: Double,
    val windSpeedKmh: Double,
    val weatherCode: Int,
    val isDay: Boolean,
    val timeIso: String
)
