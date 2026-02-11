package com.example.kotlinpet

import com.example.kotlinpet.network.OpenMeteoApi
import com.example.kotlinpet.network.createHttpClient
import com.example.kotlinpet.repository.LocationRepository
import com.example.kotlinpet.repository.WeatherRepository
import com.example.kotlinpet.repository.WeatherRepositoryImpl

class AppContainer {
    private val httpClient = createHttpClient()

    val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
        api = OpenMeteoApi(httpClient)
    )

    val locationRepository: LocationRepository = LocationRepository()
}
