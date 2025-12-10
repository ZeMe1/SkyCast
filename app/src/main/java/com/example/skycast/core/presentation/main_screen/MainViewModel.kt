package com.example.skycast.core.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skycast.core.domain.model.CurrentWeather
import com.example.skycast.core.domain.model.DailyForecastData
import com.example.skycast.core.domain.model.HourlyForecast
import com.example.skycast.core.domain.repository.WeatherForecastResult
import com.example.skycast.core.domain.weather.WeatherFacade
import com.example.skycast.core.domain.weather.WeatherRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val weatherFacade: WeatherFacade
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

            weatherFacade.fetchWeather(WeatherRequest.DeviceLocation)
                .onSuccess { payload ->
                    updateWeather(payload.forecast)
                    _location.value = payload.locationLabel
                    _isLoading.value = false
                }
                .onFailure {
                    _error.value = it.message ?: "Location unavailable"
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

    private fun updateWeather(forecast: WeatherForecastResult) {
        _currentWeather.value = forecast.currentWeather
        _dailyForecasts.value = forecast.dailyForecasts
        _hourlyForecasts.value = forecast.hourlyForecasts
    }
}