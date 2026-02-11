package com.example.kotlinpet.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezone: String? = null,
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto? = null,
    val daily: DailyForecastDto? = null
)

@Serializable
data class CurrentWeatherDto(
    val temperature: Double? = null,
    @SerialName("windspeed") val windSpeed: Double? = null,
    @SerialName("weathercode") val weatherCode: Int? = null,
    @SerialName("is_day") val isDay: Int? = null,
    val time: String? = null
)

@Serializable
data class DailyForecastDto(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m_max") val tempMax: List<Double> = emptyList(),
    @SerialName("temperature_2m_min") val tempMin: List<Double> = emptyList(),
    @SerialName("weathercode") val weatherCode: List<Int> = emptyList()
)
