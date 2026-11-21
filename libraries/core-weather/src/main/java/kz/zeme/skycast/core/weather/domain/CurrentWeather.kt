package kz.zeme.skycast.core.weather.domain

data class CurrentWeather(
    val temperature: Int,
    val apparentTemperature: Int,
    val minTemperature: Int,
    val maxTemperature: Int,
    val condition: WeatherCondition,
    val isDay: Boolean,
    val windSpeed: Double,
    val windDirection: WindDirection,
    val windGusts: Int,
    val humidity: Int,
    val dewPoint: Int,
    val pressureHpa: Int,
    val uvIndex: Int,
    val uvLevel: UvLevel,
)
