package com.example.skycast.core.presentation.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skycast.core.domain.model.CurrentWeather
import com.example.skycast.core.domain.model.DailyForecastData
import com.example.skycast.core.domain.model.HourlyForecast
import com.example.skycast.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val weatherRepository: WeatherRepository,
): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location.asStateFlow()

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

    fun onValueChange(query: String) {
        _searchQuery.value = query
    }

    fun onSearch(queryLocation: String) {
        viewModelScope.launch {
            _isLoading.value = true
            weatherRepository.getWeatherForecast(queryLocation)
                .onSuccess { result ->

                    _currentWeather.value = result.currentWeather

                    _dailyForecasts.value = result.dailyForecasts

                    _hourlyForecasts.value = result.hourlyForecasts

                    _location.value = queryLocation

                    _isLoading.value = false
                }
                .onFailure { exception ->

                    _error.value = exception.message ?: "Failed to fetch weather data"
                    _isLoading.value = false
                }
        }
    }

    fun clearTextField() {
        _searchQuery.value = ""
    }
}