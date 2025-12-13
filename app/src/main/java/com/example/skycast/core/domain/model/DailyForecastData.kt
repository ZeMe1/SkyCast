package com.example.skycast.core.domain.model


data class DailyForecastData(
    val date: String,
    val maxTempC: Double,
    val minTempC: Double,
    val sunrise: String,
    val sunset: String,
    val weatherType: WeatherType
)

