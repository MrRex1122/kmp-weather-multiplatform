package com.example.kotlinpet.repository

import com.example.kotlinpet.model.CurrentWeather
import com.example.kotlinpet.model.DailyForecast
import com.example.kotlinpet.model.Location
import com.example.kotlinpet.model.WeatherSnapshot
import com.example.kotlinpet.network.OpenMeteoApi
import kotlinx.datetime.Clock

class WeatherRepositoryImpl(
    private val api: OpenMeteoApi,
    private val clock: Clock = Clock.System
) : WeatherRepository {
    override suspend fun getWeather(location: Location): WeatherSnapshot {
        val response = api.fetchWeather(location)
        val currentDto = response.currentWeather
            ?: error("Missing current weather for ${location.name}")
        val dailyDto = response.daily
            ?: error("Missing daily forecast for ${location.name}")

        val count = listOf(
            dailyDto.time.size,
            dailyDto.tempMin.size,
            dailyDto.tempMax.size,
            dailyDto.weatherCode.size
        ).minOrNull() ?: 0

        val daily = (0 until count).map { index ->
            DailyForecast(
                dateIso = dailyDto.time[index],
                minTempC = dailyDto.tempMin[index],
                maxTempC = dailyDto.tempMax[index],
                weatherCode = dailyDto.weatherCode[index]
            )
        }

        val current = CurrentWeather(
            temperatureC = currentDto.temperature ?: 0.0,
            windSpeedKmh = currentDto.windSpeed ?: 0.0,
            weatherCode = currentDto.weatherCode ?: 0,
            isDay = (currentDto.isDay ?: 1) == 1,
            timeIso = currentDto.time ?: ""
        )

        return WeatherSnapshot(
            location = location,
            current = current,
            daily = daily,
            fetchedAtIso = clock.now().toString(),
            source = "Open-Meteo"
        )
    }
}
