package com.example.skycast.core.domain.model

import com.example.skycast.R


enum class WeatherType(
    val label: String,
    val dailyIcon: Int,
    val hourlyDayIcon: Int,
    val hourlyNightIcon: Int = hourlyDayIcon,
    val currentIcon: Int = dailyIcon,
    val isSunEvent: Boolean = false
) {
    Clear(
        label = "Sunny",
        dailyIcon = R.drawable.ic_sunny_daily,
        hourlyDayIcon = R.drawable.ic_sunny_hourly,
        hourlyNightIcon = R.drawable.ic_open_moon_hourly,
        currentIcon = R.drawable.ic_sunny_current
    ),
    PartlyCloudy(
        label = "Partly Cloudy",
        dailyIcon = R.drawable.ic_partly_cloudy_daily,
        hourlyDayIcon = R.drawable.ic_partly_cloudy_hourly,
        hourlyNightIcon = R.drawable.ic_cloudy_moon_hourly,
        currentIcon = R.drawable.ic_partly_cloudy_current
    ),
    Cloudy(
        label = "Cloudy",
        dailyIcon = R.drawable.ic_cloudy_daily,
        hourlyDayIcon = R.drawable.ic_cloudy_hourly,
        hourlyNightIcon = R.drawable.ic_cloudy_moon_hourly,
        currentIcon = R.drawable.ic_cloudy_current
    ),
    Rain(
        label = "Rain",
        dailyIcon = R.drawable.ic_cloudy_rain_daily,
        hourlyDayIcon = R.drawable.ic_partly_cloudy_rain_hourly,
        hourlyNightIcon = R.drawable.ic_cloudy_moon_rain_hourly,
        currentIcon = R.drawable.ic_cloudy_rain_current
    ),
    Snow(
        label = "Snow",
        dailyIcon = R.drawable.ic_cloudy_rain_daily,
        hourlyDayIcon = R.drawable.ic_partly_cloudy_rain_hourly,
        hourlyNightIcon = R.drawable.ic_cloudy_moon_rain_hourly,
        currentIcon = R.drawable.ic_cloudy_rain_current
    ),
    Thunder(
        label = "Thunder",
        dailyIcon = R.drawable.ic_thunder_daily,
        hourlyDayIcon = R.drawable.ic_thunder_hourly,
        currentIcon = R.drawable.ic_thunder_current
    ),
    Sunrise(
        label = "Sunrise",
        dailyIcon = R.drawable.ic_sunrise,
        hourlyDayIcon = R.drawable.ic_sunrise,
        hourlyNightIcon = R.drawable.ic_sunrise,
        isSunEvent = true
    ),
    Sunset(
        label = "Sunset",
        dailyIcon = R.drawable.ic_sunset,
        hourlyDayIcon = R.drawable.ic_sunset,
        hourlyNightIcon = R.drawable.ic_sunset,
        isSunEvent = true
    ),
    Unknown(
        label = "Unknown",
        dailyIcon = R.drawable.ic_partly_cloudy_daily,
        hourlyDayIcon = R.drawable.ic_partly_cloudy_hourly,
        hourlyNightIcon = R.drawable.ic_cloudy_moon_hourly
    );

    companion object {

        fun fromCode(code: Int): WeatherType = when (code) {
            1000 -> Clear
            1003 -> PartlyCloudy
            1006, 1009 -> Cloudy

            1063, 1150, 1153, 1168, 1171,
            1180, 1183, 1186, 1189, 1192, 1195,
            1198, 1201, 1204, 1207,
            1240, 1243, 1246, 1249, 1252 -> Rain

            1114, 1117,
            1210, 1213, 1216, 1219, 1222, 1225,
            1237, 1255, 1258, 1261, 1264 -> Snow

            1087, 1273, 1276, 1279, 1282 -> Thunder

            else -> PartlyCloudy
        }

        fun hourlyIcon(weatherType: WeatherType, isDay: Boolean): Int {
            return if (isDay) weatherType.hourlyDayIcon else weatherType.hourlyNightIcon
        }
    }
}

