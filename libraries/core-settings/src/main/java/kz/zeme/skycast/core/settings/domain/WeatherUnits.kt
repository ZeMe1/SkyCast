package kz.zeme.skycast.core.settings.domain

import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit

data class WeatherUnits(
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
)
