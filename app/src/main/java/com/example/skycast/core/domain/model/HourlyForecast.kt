package com.example.skycast.core.domain.model


data class HourlyForecast(
    val time: String,
    val tempC: Double,
    val isDay: Boolean,
    val cloud: Int,
    val willItRain: Boolean,
    val willItSnow: Boolean,
    val weatherType: WeatherType,
    val displayTemp: String,
    val type: HourDisplayType = HourDisplayType.WEATHER
)


enum class HourDisplayType {
    WEATHER,
    SUNRISE,
    SUNSET
}

