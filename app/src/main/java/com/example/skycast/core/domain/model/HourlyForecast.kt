package com.example.skycast.core.domain.model


data class HourlyForecast(
    val time: String,
    val isDay: Boolean,
    val weatherType: WeatherType,
    val displayTemp: String
)

