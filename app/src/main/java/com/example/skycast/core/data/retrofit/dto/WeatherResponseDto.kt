package com.example.skycast.core.data.retrofit.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponseDto(
    @SerialName("current")
    val current: CurrentWeatherDto = CurrentWeatherDto(),
    @SerialName("forecast")
    val forecast: ForecastDto = ForecastDto()
)


@Serializable
data class CurrentWeatherDto(
    @SerialName("temp_c")
    val tempC: Double = 0.0,
    @SerialName("is_day")
    val isDay: Int = 1,
    @SerialName("cloud")
    val cloud: Int = 0,
    @SerialName("feelslike_c")
    val feelslikeC: Double = 0.0,
    @SerialName("condition")
    val condition: ConditionDto = ConditionDto()
)


@Serializable
data class ForecastDto(
    @SerialName("forecastday")
    val forecastday: List<ForecastDayDto> = emptyList()
)


@Serializable
data class ForecastDayDto(
    @SerialName("date")
    val date: String = "",
    @SerialName("day")
    val day: DayDto = DayDto(),
    @SerialName("astro")
    val astro: AstroDto = AstroDto(),
    @SerialName("hour")
    val hour: List<HourDto> = emptyList()
)


@Serializable
data class DayDto(
    @SerialName("maxtemp_c")
    val maxTempC: Double = 0.0,
    @SerialName("mintemp_c")
    val minTempC: Double = 0.0,
    @SerialName("daily_will_it_rain")
    val dailyWillItRain: Int = 0,
    @SerialName("daily_will_it_snow")
    val dailyWillItSnow: Int = 0,
    @SerialName("condition")
    val condition: ConditionDto = ConditionDto()
)


@Serializable
data class AstroDto(
    @SerialName("sunrise")
    val sunrise: String = "--:--",
    @SerialName("sunset")
    val sunset: String = "--:--"
)


@Serializable
data class HourDto(
    @SerialName("time")
    val time: String = "",
    @SerialName("temp_c")
    val tempC: Double = 0.0,
    @SerialName("is_day")
    val isDay: Int = 1,
    @SerialName("cloud")
    val cloud: Int = 0,
    @SerialName("will_it_rain")
    val willItRain: Int = 0,
    @SerialName("will_it_snow")
    val willItSnow: Int = 0,
    @SerialName("condition")
    val condition: ConditionDto = ConditionDto()
)


@Serializable
data class ConditionDto(
    @SerialName("code")
    val code: Int = 1003 // partly cloudy default
)

