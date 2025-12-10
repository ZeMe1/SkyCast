package com.example.skycast.core.domain.weather

import com.example.skycast.core.domain.repository.WeatherForecastResult

interface WeatherFetchStrategy {
    suspend fun fetch(): Result<WeatherPayload>
}

