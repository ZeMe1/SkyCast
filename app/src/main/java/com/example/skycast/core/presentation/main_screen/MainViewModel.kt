package com.example.skycast.core.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skycast.core.domain.model.CurrentWeather
import com.example.skycast.core.domain.model.DailyForecastData
import com.example.skycast.core.domain.model.HourlyForecast
import com.example.skycast.core.domain.repository.LocationRepository
import com.example.skycast.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _currentWeather = MutableStateFlow<CurrentWeather?>(null)
    val currentWeather: StateFlow<CurrentWeather?> = _currentWeather.asStateFlow()

    private val _dailyForecasts = MutableStateFlow<List<DailyForecastData>>(emptyList())
    val dailyForecasts: StateFlow<List<DailyForecastData>> = _dailyForecasts.asStateFlow()

    private val _hourlyForecasts = MutableStateFlow<List<HourlyForecast>>(emptyList())
    val hourlyForecasts: StateFlow<List<HourlyForecast>> = _hourlyForecasts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location.asStateFlow()


    fun fetchWeatherForCurrentLocation() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val locationResult = locationRepository.getCurrentLocation()
            locationResult.onSuccess { loc ->
                _location.value = loc.label
                fetchWeatherForecast(location = "${loc.latitude},${loc.longitude}", updateLocation = false)
            }.onFailure {
                _error.value = it.message ?: "Location unavailable"
                _isLoading.value = false
            }
        }
    }


    fun fetchWeatherForecast(location: String, updateLocation: Boolean = true) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            weatherRepository.getWeatherForecast(location)
                .onSuccess { result ->

                    _currentWeather.value = result.currentWeather

                    _dailyForecasts.value = result.dailyForecasts

                    _hourlyForecasts.value = result.hourlyForecasts

                    if (updateLocation) {
                        _location.value = location
                    }
                    _isLoading.value = false
                }
                .onFailure { exception ->

                    _error.value = exception.message ?: "Failed to fetch weather data"
                    _isLoading.value = false
                }
        }
    }

    fun setError(message: String) {
        _error.value = message
    }


    fun clearError() {
        _error.value = null
    }
}