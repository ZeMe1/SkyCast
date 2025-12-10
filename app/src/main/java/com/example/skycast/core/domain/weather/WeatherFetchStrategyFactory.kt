package com.example.skycast.core.domain.weather

import com.example.skycast.core.domain.repository.LocationRepository
import com.example.skycast.core.domain.repository.WeatherRepository
import com.example.skycast.core.domain.weather.strategy.DeviceLocationWeatherStrategy
import com.example.skycast.core.domain.weather.strategy.QueryWeatherStrategy

class WeatherFetchStrategyFactory(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) {
    fun create(request: WeatherRequest): WeatherFetchStrategy {
        return when (request) {
            is WeatherRequest.DeviceLocation -> DeviceLocationWeatherStrategy(
                locationRepository = locationRepository,
                weatherRepository = weatherRepository
            )
            is WeatherRequest.Query -> QueryWeatherStrategy(
                weatherRepository = weatherRepository,
                query = request.value
            )
        }
    }
}

