package com.example.skycast.core.domain.weather

interface WeatherFetchStrategy {
    suspend fun fetch(): Result<WeatherPayload>
}

