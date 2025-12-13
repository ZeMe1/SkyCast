package com.example.skycast.core.data.retrofit.dto

import com.example.skycast.core.domain.model.CurrentWeather
import com.example.skycast.core.domain.model.DailyForecastData
import com.example.skycast.core.domain.model.HourlyForecast
import com.example.skycast.core.domain.model.WeatherType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun CurrentWeatherDto.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        tempC = this.tempC,
        feelslikeC = this.feelslikeC,
        weatherType = WeatherType.fromCode(this.condition.code)
    )
}


fun ForecastDayDto.toDailyForecastData(): DailyForecastData {
    return DailyForecastData(
        date = this.date,
        maxTempC = this.day.maxTempC,
        minTempC = this.day.minTempC,
        sunrise = this.astro.sunrise,
        sunset = this.astro.sunset,
        weatherType = WeatherType.fromCode(this.day.condition.code)
    )
}


fun HourDto.toHourlyForecast(): Pair<LocalDateTime, HourlyForecast>? {
    val parsedDateTime = runCatching { LocalDateTime.parse(this.time, apiDateTimeFormatter) }
        .getOrElse { return null }

    val displayTime = formatWeatherHour(parsedDateTime)

    val forecast = HourlyForecast(
        time = displayTime,
        isDay = this.isDay == 1,
        weatherType = WeatherType.fromCode(this.condition.code),
        displayTemp = formatTemp(this.tempC)
    )

    return parsedDateTime to forecast
}

internal fun createSunEvent(
    date: LocalDate,
    timeString: String,
    isSunrise: Boolean
): Pair<LocalDateTime, HourlyForecast>? {
    val parsedTime = runCatching {
        LocalTime.parse(timeString, sunTimeFormatter)
    }.getOrElse { return null }

    val dateTime = date.atTime(parsedTime)
    val displayTime = sunDisplayFormatter.format(parsedTime)
    val weatherType = if (isSunrise) WeatherType.Sunrise else WeatherType.Sunset

    val forecast = HourlyForecast(
        time = displayTime,
        isDay = isSunrise, // sunrise -> day, sunset -> night
        weatherType = weatherType,
        displayTemp = weatherType.label
    )

    return dateTime to forecast
}

private fun formatWeatherHour(dateTime: LocalDateTime): String {

    val hour = hourFormatter.format(dateTime)
    return "$hour:00"
}

private fun formatTemp(temp: Double): String = "${temp.toInt()}°"

private fun String.toSafeLocalDate(): LocalDate {
    return runCatching { LocalDate.parse(this, apiDateFormatter) }
        .getOrElse { LocalDate.now() }
}


fun ForecastDto.next24Hours(): List<HourlyForecast> {
    val now = LocalDateTime.now()

    val all = forecastday.flatMap { day ->
        val dateObj = day.date.toSafeLocalDate()

        val weatherHours = day.hour.mapNotNull { it.toHourlyForecast() }
        val sunEvents = listOfNotNull(
            createSunEvent(dateObj, day.astro.sunrise, true),
            createSunEvent(dateObj, day.astro.sunset, false)
        )

        (weatherHours + sunEvents)
    }

    val sorted = all.sortedBy { it.first }
    val upcoming = sorted.filter { !it.first.isBefore(now) }.take(24)
    return upcoming.map { it.second }
}

private val apiDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
private val apiDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val sunTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)
private val hourFormatter = DateTimeFormatter.ofPattern("HH")
private val sunDisplayFormatter = DateTimeFormatter.ofPattern("HH:mm")

