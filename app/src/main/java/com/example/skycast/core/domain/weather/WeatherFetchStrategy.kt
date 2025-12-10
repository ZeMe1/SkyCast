package com.example.skycast.core.domain.weather

import com.example.skycast.core.domain.repository.WeatherForecastResult

/**
 * Strategy contract describing how to fetch a weather forecast.
 * Implementations encapsulate the retrieval mechanism (query, GPS, etc.).
 */
interface WeatherFetchStrategy {
    suspend fun fetch(): Result<WeatherPayload>
}

