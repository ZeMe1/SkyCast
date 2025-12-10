package com.example.skycast.core.domain.weather.strategy

import com.example.skycast.core.domain.repository.LocationRepository
import com.example.skycast.core.domain.repository.WeatherRepository
import com.example.skycast.core.domain.weather.WeatherFetchStrategy
import com.example.skycast.core.domain.weather.WeatherPayload

/**
 * Behavioral Strategy: fetches weather by first resolving the device location.
 */
class DeviceLocationWeatherStrategy(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : WeatherFetchStrategy {

    override suspend fun fetch(): Result<WeatherPayload> {
        val locationResult = locationRepository.getCurrentLocation()
        if (locationResult.isFailure) {
            return Result.failure(locationResult.exceptionOrNull() ?: IllegalStateException("Unknown location error"))
        }

        val deviceLocation = locationResult.getOrNull()
            ?: return Result.failure(IllegalStateException("Location unavailable"))

        val query = "${deviceLocation.latitude},${deviceLocation.longitude}"

        return weatherRepository.getWeatherForecast(query).map { forecast ->
            WeatherPayload(
                locationLabel = deviceLocation.label,
                forecast = forecast
            )
        }
    }
}

