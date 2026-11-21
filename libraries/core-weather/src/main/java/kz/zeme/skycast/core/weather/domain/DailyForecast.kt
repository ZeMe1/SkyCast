package kz.zeme.skycast.core.weather.domain

import java.time.DayOfWeek

data class DailyForecast(
    val dateIso: String,
    val dayOfWeek: DayOfWeek,
    val isToday: Boolean,
    val condition: WeatherCondition,
    val minTemperature: Int,
    val maxTemperature: Int,
    val rangeStartFraction: Float,
    val rangeWidthFraction: Float,
)
