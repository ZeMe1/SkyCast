package kz.zeme.skycast.settings.presentation.main

import androidx.annotation.StringRes
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit

data class SettingsState(
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val languageCode: LanguageCode,
)

@StringRes
internal fun TemperatureUnit.toShortStringRes(): Int = when (this) {
    TemperatureUnit.Celsius -> R.string.unit_temp_celsius_short
    TemperatureUnit.Fahrenheit -> R.string.unit_temp_fahrenheit_short
}

@StringRes
internal fun WindSpeedUnit.toShortStringRes(): Int = when (this) {
    WindSpeedUnit.MetersPerSecond -> R.string.unit_wind_ms_short
    WindSpeedUnit.KilometersPerHour -> R.string.unit_wind_kmh_short
}

@StringRes
internal fun LanguageCode.toDisplayStringRes(): Int = when (this) {
    LanguageCode.Ru -> R.string.language_picker_language_ru
    LanguageCode.Kz -> R.string.language_picker_language_kz
    LanguageCode.En -> R.string.language_picker_language_en
}
