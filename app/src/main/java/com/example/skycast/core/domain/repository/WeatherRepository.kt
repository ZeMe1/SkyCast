package com.example.skycast.core.domain.repository

import com.example.skycast.core.domain.model.CurrentWeather
import com.example.skycast.core.domain.model.DailyForecastData
import com.example.skycast.core.domain.model.HourlyForecast


interface WeatherRepository {

    suspend fun getWeatherForecast(location: String): Result<WeatherForecastResult>
}


data class WeatherForecastResult(
    val currentWeather: CurrentWeather,
    val dailyForecasts: List<DailyForecastData>,
    val hourlyForecasts: List<HourlyForecast>
)


