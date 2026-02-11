package com.example.kotlinpet.model

data class DailyForecast(
    val dateIso: String,
    val minTempC: Double,
    val maxTempC: Double,
    val weatherCode: Int
)
