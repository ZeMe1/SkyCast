package com.example.skycast.core.domain.weather

class WeatherFacade(
    private val strategyFactory: WeatherFetchStrategyFactory
) {
    suspend fun fetchWeather(request: WeatherRequest): Result<WeatherPayload> {
        val strategy = strategyFactory.create(request)
        return strategy.fetch()
    }
}

