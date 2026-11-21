package kz.zeme.skycast.core.weather.domain

import kotlinx.collections.immutable.ImmutableList
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit

data class WeatherForecast(
    val current: CurrentWeather,
    val hourlyTimeline: ImmutableList<HourlyTimelineItem>,
    val daily: ImmutableList<DailyForecast>,
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
)
