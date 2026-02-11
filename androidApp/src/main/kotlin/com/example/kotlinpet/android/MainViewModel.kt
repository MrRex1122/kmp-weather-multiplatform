package com.example.kotlinpet.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinpet.AppContainer
import com.example.kotlinpet.model.Location
import com.example.kotlinpet.model.WeatherSnapshot
import com.example.kotlinpet.repository.LocationRepository
import com.example.kotlinpet.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Loaded(val snapshot: WeatherSnapshot) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class MainViewModel(
    container: AppContainer = AppContainer()
) : ViewModel() {
    private val weatherRepository: WeatherRepository = container.weatherRepository
    private val locationRepository: LocationRepository = container.locationRepository

    val locations: List<Location> = locationRepository.defaultLocations()

    var selectedLocation: Location = locations.firstOrNull()
        ?: Location(name = "Unknown", latitude = 0.0, longitude = 0.0)
        private set

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun selectLocation(location: Location) {
        selectedLocation = location
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading
            runCatching { weatherRepository.getWeather(selectedLocation) }
                .onSuccess { snapshot ->
                    _state.value = WeatherUiState.Loaded(snapshot)
                }
                .onFailure { throwable ->
                    _state.value = WeatherUiState.Error(
                        throwable.message ?: "Unknown error"
                    )
                }
        }
    }
}
