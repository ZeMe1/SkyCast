package kz.zeme.skycast.home.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.DayOfWeek
import java.util.Locale
import kz.zeme.skycast.core.error.BaseError
import kz.zeme.skycast.core.error.ConnectionError
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import kz.zeme.skycast.core.weather.domain.SunEventType
import kz.zeme.skycast.core.weather.domain.UvLevel
import kz.zeme.skycast.core.weather.domain.WeatherCondition
import kz.zeme.skycast.core.weather.domain.WindDirection

internal fun Int.formatSignedTemperature(): String = when {
    this > 0 -> "+$this°"
    else -> "$this°"
}

internal fun Int.formatPercent(): String = "$this%"

internal fun Double.formatWindSpeed(): String = String.format(Locale.US, "%.1f", this)

@StringRes
internal fun WindSpeedUnit.toValueFormatRes(): Int = when (this) {
    WindSpeedUnit.MetersPerSecond -> R.string.unit_wind_ms_value
    WindSpeedUnit.KilometersPerHour -> R.string.unit_wind_kmh_value
}

@StringRes
internal fun WeatherCondition.toTitleRes(): Int = when (this) {
    WeatherCondition.Clear -> R.string.weather_condition_clear
    WeatherCondition.MainlyClear -> R.string.weather_condition_mainly_clear
    WeatherCondition.PartlyCloudy -> R.string.weather_condition_partly_cloudy
    WeatherCondition.Overcast -> R.string.weather_condition_overcast
    WeatherCondition.Fog -> R.string.weather_condition_fog
    WeatherCondition.Drizzle -> R.string.weather_condition_drizzle
    WeatherCondition.Rain -> R.string.weather_condition_rain
    WeatherCondition.Snow -> R.string.weather_condition_snow
    WeatherCondition.Thunderstorm -> R.string.weather_condition_thunderstorm
}

@DrawableRes
internal fun WeatherCondition.toIconRes(isDay: Boolean): Int = when (this) {
    WeatherCondition.Clear ->
        if (isDay) R.drawable.ic_weather_sun
        else R.drawable.ic_weather_moon
    WeatherCondition.MainlyClear,
    WeatherCondition.PartlyCloudy ->
        if (isDay) R.drawable.ic_weather_partly_cloudy
        else R.drawable.ic_weather_moon
    WeatherCondition.Overcast,
    WeatherCondition.Fog -> R.drawable.ic_weather_cloudy
    WeatherCondition.Drizzle,
    WeatherCondition.Rain,
    WeatherCondition.Thunderstorm -> R.drawable.ic_weather_rain
    WeatherCondition.Snow -> R.drawable.ic_weather_snow
}

@DrawableRes
internal fun SunEventType.toIconRes(): Int = when (this) {
    SunEventType.Sunrise -> R.drawable.ic_weather_sunrise
    SunEventType.Sunset -> R.drawable.ic_weather_sunset
}

@StringRes
internal fun SunEventType.toTitleRes(): Int = when (this) {
    SunEventType.Sunrise -> R.string.home_sunrise
    SunEventType.Sunset -> R.string.home_sunset
}

@StringRes
internal fun WindDirection.toShortRes(): Int = when (this) {
    WindDirection.North -> R.string.wind_dir_n
    WindDirection.NorthEast -> R.string.wind_dir_ne
    WindDirection.East -> R.string.wind_dir_e
    WindDirection.SouthEast -> R.string.wind_dir_se
    WindDirection.South -> R.string.wind_dir_s
    WindDirection.SouthWest -> R.string.wind_dir_sw
    WindDirection.West -> R.string.wind_dir_w
    WindDirection.NorthWest -> R.string.wind_dir_nw
}

@StringRes
internal fun UvLevel.toTitleRes(): Int = when (this) {
    UvLevel.Low -> R.string.uv_level_low
    UvLevel.Moderate -> R.string.uv_level_moderate
    UvLevel.High -> R.string.uv_level_high
    UvLevel.VeryHigh -> R.string.uv_level_very_high
    UvLevel.Extreme -> R.string.uv_level_extreme
}

internal fun UvLevel.isLowOrModerate(): Boolean =
    this == UvLevel.Low || this == UvLevel.Moderate

@StringRes
internal fun DayOfWeek.toShortRes(): Int = when (this) {
    DayOfWeek.MONDAY -> R.string.weekday_short_mon
    DayOfWeek.TUESDAY -> R.string.weekday_short_tue
    DayOfWeek.WEDNESDAY -> R.string.weekday_short_wed
    DayOfWeek.THURSDAY -> R.string.weekday_short_thu
    DayOfWeek.FRIDAY -> R.string.weekday_short_fri
    DayOfWeek.SATURDAY -> R.string.weekday_short_sat
    DayOfWeek.SUNDAY -> R.string.weekday_short_sun
}

@StringRes
internal fun BaseError?.toErrorTitleRes(): Int = when (this) {
    is ConnectionError -> R.string.home_no_connection
    else -> R.string.shared_something_went_wrong
}

@StringRes
internal fun BaseError?.toErrorMessageRes(): Int = when (this) {
    is ConnectionError -> R.string.home_error_network
    else -> R.string.shared_unknown_error
}
