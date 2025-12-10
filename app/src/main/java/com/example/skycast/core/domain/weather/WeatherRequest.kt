package com.example.skycast.core.domain.weather

import com.example.skycast.core.domain.repository.WeatherForecastResult

/**
 * Represents a high-level weather request coming from the UI.
 */
sealed interface WeatherRequest {
    /**
     * Fetch weather using the device GPS location.
     */
    data object DeviceLocation : WeatherRequest

    /**
     * Fetch weather using a free-form location query (city, coordinates, etc.).
     */
    data class Query(val value: String) : WeatherRequest
}

/**
 * Wrapper that carries both the resolved location label (for display) and the forecast data.
 */
data class WeatherPayload(
    val locationLabel: String,
    val forecast: WeatherForecastResult
)

