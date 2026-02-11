package com.example.kotlinpet.repository

import com.example.kotlinpet.model.Location
import com.example.kotlinpet.model.WeatherSnapshot

interface WeatherRepository {
    suspend fun getWeather(location: Location): WeatherSnapshot
}
