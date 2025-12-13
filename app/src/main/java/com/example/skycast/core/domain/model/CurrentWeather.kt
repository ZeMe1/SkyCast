package com.example.skycast.core.domain.model


data class CurrentWeather(
    val tempC: Double,
    val feelslikeC: Double,
    val weatherType: WeatherType
)

