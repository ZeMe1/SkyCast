package com.example.skycast.core.domain.model


data class CurrentWeather(
    val tempC: Double,
    val isDay: Boolean,
    val cloud: Int,
    val feelslikeC: Double,
    val weatherType: WeatherType
)

