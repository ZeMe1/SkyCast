package com.example.skycast.core.domain.weather

import com.example.skycast.core.domain.repository.WeatherForecastResult

sealed interface WeatherRequest {
        data object DeviceLocation : WeatherRequest
        data class Query(val value: String) : WeatherRequest
}

data class WeatherPayload(
    val locationLabel: String,
    val forecast: WeatherForecastResult
)

