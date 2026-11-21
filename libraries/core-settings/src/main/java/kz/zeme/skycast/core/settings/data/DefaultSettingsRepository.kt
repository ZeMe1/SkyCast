package kz.zeme.skycast.core.settings.data

import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.settings.domain.SettingsRepository
import kz.zeme.skycast.core.settings.domain.TemperatureMetric
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WeatherUnits
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit

class DefaultSettingsRepository(
    private val settingsLocalDataSource: SettingsLocalDataSource,
) : SettingsRepository {

    override fun getWeatherUnits(): WeatherUnits = WeatherUnits(
        temperatureUnit = settingsLocalDataSource.getSelectedTemperatureUnit(TemperatureUnit.Celsius),
        windSpeedUnit = settingsLocalDataSource.getSelectedWindSpeedUnit(WindSpeedUnit.MetersPerSecond),
    )

    override suspend fun getTemperatureMetrics(): Response<List<TemperatureMetric>> {
        val selectedUnit = settingsLocalDataSource.getSelectedTemperatureUnit(TemperatureUnit.Celsius)
        return Response.Success(
            TemperatureUnit.entries.map { unit ->
                TemperatureMetric(
                    isSelected = selectedUnit == unit,
                    code = unit,
                )
            },
        )
    }

    override suspend fun saveSelectedTemperatureMetric(metric: TemperatureMetric): Response<Unit> {
        settingsLocalDataSource.saveSelectedTemperatureUnit(metric.code)
        return Response.Success(Unit)
    }

    override suspend fun getWindSpeedMetrics(): Response<List<WindSpeedMetric>> {
        val selectedUnit = settingsLocalDataSource.getSelectedWindSpeedUnit(WindSpeedUnit.MetersPerSecond)
        return Response.Success(
            WindSpeedUnit.entries.map { unit ->
                WindSpeedMetric(
                    isSelected = selectedUnit == unit,
                    code = unit,
                )
            },
        )
    }

    override suspend fun saveSelectedWindSpeedMetric(metric: WindSpeedMetric): Response<Unit> {
        settingsLocalDataSource.saveSelectedWindSpeedUnit(metric.code)
        return Response.Success(Unit)
    }
}
