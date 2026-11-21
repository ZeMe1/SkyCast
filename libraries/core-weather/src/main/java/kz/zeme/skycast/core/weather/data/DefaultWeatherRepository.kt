package kz.zeme.skycast.core.weather.data

import com.google.gson.Gson
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.location.domain.LocationRepository
import kz.zeme.skycast.core.network.extensions.mapFailure
import kz.zeme.skycast.core.network.extensions.tryMapSuspended
import kz.zeme.skycast.core.settings.domain.SettingsRepository
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.TemperatureUnitApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.WindSpeedUnitApi
import kz.zeme.skycast.core.weather.domain.WeatherForecast
import kz.zeme.skycast.core.weather.domain.WeatherRepository

class DefaultWeatherRepository(
    private val networkDataSource: WeatherNetworkDataSource,
    private val locationRepository: LocationRepository,
    private val settingsRepository: SettingsRepository,
    private val apiMapper: WeatherApiMapper,
    private val gson: Gson,
) : WeatherRepository {

    override suspend fun getWeatherForecast(): Response<WeatherForecast> {
        val location = when (val locationResponse = locationRepository.getCurrentLocation()) {
            is Response.Success -> locationResponse.data
            is Response.Failure -> return Response.Failure(locationResponse.data)
        }
        val units = settingsRepository.getWeatherUnits()

        return tryMapSuspended(
            request = {
                networkDataSource.getForecast(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    temperatureUnit = units.temperatureUnit.toApi(),
                    windSpeedUnit = units.windSpeedUnit.toApi(),
                )
            },
            mapSuccess = { apiDto ->
                apiMapper.map(apiDto, units)
            },
            mapFailure = gson::mapFailure,
        )
    }

    private fun TemperatureUnit.toApi(): TemperatureUnitApi = when (this) {
        TemperatureUnit.Celsius -> TemperatureUnitApi.Celsius
        TemperatureUnit.Fahrenheit -> TemperatureUnitApi.Fahrenheit
    }

    private fun WindSpeedUnit.toApi(): WindSpeedUnitApi = when (this) {
        WindSpeedUnit.MetersPerSecond -> WindSpeedUnitApi.MetersPerSecond
        WindSpeedUnit.KilometersPerHour -> WindSpeedUnitApi.KilometersPerHour
    }
}
