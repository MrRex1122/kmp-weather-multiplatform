package com.example.kotlinpet.repository

import com.example.kotlinpet.model.Location

class LocationRepository {
    fun defaultLocations(): List<Location> = listOf(
        Location(name = "Moscow", latitude = 55.7558, longitude = 37.6173),
        Location(name = "Saint Petersburg", latitude = 59.9343, longitude = 30.3351),
        Location(name = "London", latitude = 51.5074, longitude = -0.1278),
        Location(name = "New York", latitude = 40.7128, longitude = -74.0060),
        Location(name = "Tokyo", latitude = 35.6762, longitude = 139.6503)
    )

    fun defaultLocation(): Location = defaultLocations().first()
}
