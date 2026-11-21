package kz.zeme.skycast.settings.presentation.unitpicker

import kz.zeme.skycast.core.settings.domain.TemperatureMetric
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit

data class UnitPickerItem(
    val isSelected: Boolean,
    val code: UnitCode,
) {

    enum class UnitCode {
        Celsius,
        Fahrenheit,
        MetersPerSecond,
        KilometersPerHour,
    }
}

fun TemperatureMetric.toUnitPickerItem(): UnitPickerItem = UnitPickerItem(
    isSelected = isSelected,
    code = when (code) {
        TemperatureUnit.Celsius -> UnitPickerItem.UnitCode.Celsius
        TemperatureUnit.Fahrenheit -> UnitPickerItem.UnitCode.Fahrenheit
    },
)

fun WindSpeedMetric.toUnitPickerItem(): UnitPickerItem = UnitPickerItem(
    isSelected = isSelected,
    code = when (code) {
        WindSpeedUnit.MetersPerSecond -> UnitPickerItem.UnitCode.MetersPerSecond
        WindSpeedUnit.KilometersPerHour -> UnitPickerItem.UnitCode.KilometersPerHour
    },
)

fun UnitPickerItem.toTemperatureMetric(): TemperatureMetric = TemperatureMetric(
    isSelected = isSelected,
    code = when (code) {
        UnitPickerItem.UnitCode.Fahrenheit -> TemperatureUnit.Fahrenheit
        else -> TemperatureUnit.Celsius
    },
)

fun UnitPickerItem.toWindSpeedMetric(): WindSpeedMetric = WindSpeedMetric(
    isSelected = isSelected,
    code = when (code) {
        UnitPickerItem.UnitCode.KilometersPerHour -> WindSpeedUnit.KilometersPerHour
        else -> WindSpeedUnit.MetersPerSecond
    },
)
