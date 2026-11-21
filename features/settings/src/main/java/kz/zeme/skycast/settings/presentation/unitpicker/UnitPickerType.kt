package kz.zeme.skycast.settings.presentation.unitpicker

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerItem.UnitCode

@Parcelize
enum class UnitPickerType : Parcelable {
    Temperature,
    WindSpeed,
}

@StringRes
internal fun UnitPickerType.toTitleRes(): Int = when (this) {
    UnitPickerType.Temperature -> R.string.settings_temperature
    UnitPickerType.WindSpeed -> R.string.settings_wind_speed
}

@StringRes
internal fun UnitCode.toTitleRes(): Int = when (this) {
    UnitCode.Celsius -> R.string.unit_picker_celsius
    UnitCode.Fahrenheit -> R.string.unit_picker_fahrenheit
    UnitCode.MetersPerSecond -> R.string.unit_picker_ms
    UnitCode.KilometersPerHour -> R.string.unit_picker_kmh
}
