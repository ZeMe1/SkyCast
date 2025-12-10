package com.example.skycast.core.domain.weather

/**
 * Structural Facade: provides a simple entry-point to fetch weather, hiding strategy selection.
 */
class WeatherFacade(
    private val strategyFactory: WeatherFetchStrategyFactory
) {
    suspend fun fetchWeather(request: WeatherRequest): Result<WeatherPayload> {
        val strategy = strategyFactory.create(request)
        return strategy.fetch()
    }
}

