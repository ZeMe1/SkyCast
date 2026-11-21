package kz.zeme.skycast.core.settings.domain

import kz.zeme.skycast.core.domain.Response

interface SettingsRepository {

    fun getWeatherUnits(): WeatherUnits

    suspend fun getTemperatureMetrics(): Response<List<TemperatureMetric>>

    suspend fun saveSelectedTemperatureMetric(metric: TemperatureMetric): Response<Unit>

    suspend fun getWindSpeedMetrics(): Response<List<WindSpeedMetric>>

    suspend fun saveSelectedWindSpeedMetric(metric: WindSpeedMetric): Response<Unit>
}
