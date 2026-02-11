package com.example.kotlinpet.model

data class WeatherSnapshot(
    val location: Location,
    val current: CurrentWeather,
    val daily: List<DailyForecast>,
    val fetchedAtIso: String,
    val source: String
)
