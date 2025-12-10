package com.example.skycast.core.data.repository

import com.example.skycast.core.data.retrofit.WeatherApiService
import com.example.skycast.core.data.retrofit.dto.next24Hours
import com.example.skycast.core.data.retrofit.dto.toCurrentWeather
import com.example.skycast.core.data.retrofit.dto.toDailyForecastData
import com.example.skycast.core.domain.repository.WeatherForecastResult
import com.example.skycast.core.domain.repository.WeatherRepository


class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val apiKey: String
) : WeatherRepository {


    override suspend fun getWeatherForecast(location: String): Result<WeatherForecastResult> {
        return try {

            val response = apiService.getWeatherForecast(
                apiKey = apiKey,
                location = location,
                days = 7
            )

            val currentWeather = response.current.toCurrentWeather()

            val dailyForecasts = response.forecast.forecastday.map { forecastDay ->
                forecastDay.toDailyForecastData()
            }

            val hourlyForecasts = response.forecast.next24Hours()

            Result.success(
                WeatherForecastResult(
                    currentWeather = currentWeather,
                    dailyForecasts = dailyForecasts,
                    hourlyForecasts = hourlyForecasts
                )
            )
        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}

