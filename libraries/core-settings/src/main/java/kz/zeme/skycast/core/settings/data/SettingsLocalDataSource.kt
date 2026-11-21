package kz.zeme.skycast.core.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit

class SettingsLocalDataSource(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY"
        private const val WIND_SPEED_UNIT_KEY = "WIND_SPEED_UNIT_KEY"
    }

    fun getSelectedTemperatureUnit(defaultUnit: TemperatureUnit): TemperatureUnit =
        getSelectedUnit(
            key = TEMPERATURE_UNIT_KEY,
            defaultUnit = defaultUnit,
            find = TemperatureUnit::find,
        )

    fun saveSelectedTemperatureUnit(temperatureUnit: TemperatureUnit?) =
        saveSelectedUnit(
            key = TEMPERATURE_UNIT_KEY,
            code = temperatureUnit?.code,
        )

    fun getSelectedWindSpeedUnit(defaultUnit: WindSpeedUnit): WindSpeedUnit =
        getSelectedUnit(
            key = WIND_SPEED_UNIT_KEY,
            defaultUnit = defaultUnit,
            find = WindSpeedUnit::find,
        )

    fun saveSelectedWindSpeedUnit(windSpeedUnit: WindSpeedUnit?) =
        saveSelectedUnit(
            key = WIND_SPEED_UNIT_KEY,
            code = windSpeedUnit?.code,
        )

    private fun <T> getSelectedUnit(
        key: String,
        defaultUnit: T,
        find: (String?, T) -> T,
    ): T = sharedPreferences.getString(key, null)
        ?.let { find(it, defaultUnit) }
        ?: defaultUnit

    private fun saveSelectedUnit(
        key: String,
        code: String?,
    ) {
        code ?: return

        sharedPreferences.edit(commit = true) {
            putString(key, code)
        }
    }
}
