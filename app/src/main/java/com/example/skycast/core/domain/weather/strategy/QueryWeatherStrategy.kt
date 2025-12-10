package com.example.skycast.core.domain.weather.strategy

import com.example.skycast.core.domain.repository.WeatherRepository
import com.example.skycast.core.domain.weather.WeatherFetchStrategy
import com.example.skycast.core.domain.weather.WeatherPayload

/**
 * Behavioral Strategy: fetches weather using a user-provided query string.
 */
class QueryWeatherStrategy(
    private val weatherRepository: WeatherRepository,
    private val query: String
) : WeatherFetchStrategy {

    override suspend fun fetch(): Result<WeatherPayload> {
        return weatherRepository.getWeatherForecast(query).map { forecast ->
            WeatherPayload(
                locationLabel = query,
                forecast = forecast
            )
        }
    }
}

