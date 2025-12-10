package com.example.skycast.core.domain.model


data class DailyForecastData(
    val date: String,
    val maxTempC: Double,
    val minTempC: Double,
    val dailyWillItRain: Boolean,
    val dailyWillItSnow: Boolean,
    val sunrise: String,
    val sunset: String,
    val hourlyForecasts: List<HourlyForecast>,
    val weatherType: WeatherType
)

