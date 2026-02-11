package com.example.kotlinpet.network

import com.example.kotlinpet.model.Location
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class OpenMeteoApi(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://api.open-meteo.com/v1/forecast"
) {
    suspend fun fetchWeather(location: Location): OpenMeteoResponse {
        return httpClient.get(baseUrl) {
            parameter("latitude", location.latitude)
            parameter("longitude", location.longitude)
            parameter("current_weather", true)
            parameter("daily", "weathercode,temperature_2m_max,temperature_2m_min")
            parameter("forecast_days", 7)
            parameter("timezone", "auto")
        }.body()
    }
}
