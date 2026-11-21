package kz.zeme.skycast.core.weather.data

import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.runBlocking
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.error.ConnectionError
import kz.zeme.skycast.core.error.NotFoundError
import kz.zeme.skycast.core.error.ServerError
import kz.zeme.skycast.core.location.domain.LocationPoint
import kz.zeme.skycast.core.location.domain.LocationRepository
import kz.zeme.skycast.core.settings.domain.SettingsRepository
import kz.zeme.skycast.core.settings.domain.TemperatureMetric
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WeatherUnits
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.CurrentWeatherApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.DailyWeatherApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.TemperatureUnitApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.WindSpeedUnitApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultWeatherRepositoryTest {

    @Test
    fun `getWeatherForecast uses location and units and returns mapped forecast`() = runBlocking {
        val networkDataSource = FakeWeatherNetworkDataSource {
            retrofit2.Response.success(createMinimalForecastApi())
        }
        val repository = DefaultWeatherRepository(
            networkDataSource = networkDataSource,
            locationRepository = FakeLocationRepository(
                Response.Success(LocationPoint(latitude = 51.1694, longitude = 71.4491)),
            ),
            settingsRepository = FakeSettingsRepository(
                WeatherUnits(
                    temperatureUnit = TemperatureUnit.Fahrenheit,
                    windSpeedUnit = WindSpeedUnit.KilometersPerHour,
                ),
            ),
            apiMapper = WeatherApiMapper(),
            gson = Gson(),
        )

        val response = repository.getWeatherForecast()

        assertTrue(response is Response.Success)
        val forecast = (response as Response.Success).data
        assertEquals(51.1694, networkDataSource.lastLatitude ?: 0.0, 0.0001)
        assertEquals(71.4491, networkDataSource.lastLongitude ?: 0.0, 0.0001)
        assertEquals(TemperatureUnitApi.Fahrenheit, networkDataSource.lastTemperatureUnit)
        assertEquals(WindSpeedUnitApi.KilometersPerHour, networkDataSource.lastWindSpeedUnit)
        assertEquals(57, forecast.current.temperature)
        assertEquals(TemperatureUnit.Fahrenheit, forecast.temperatureUnit)
        assertEquals(WindSpeedUnit.KilometersPerHour, forecast.windSpeedUnit)
    }

    @Test
    fun `getWeatherForecast returns Failure without network call when location fails`() = runBlocking {
        val networkDataSource = FakeWeatherNetworkDataSource {
            retrofit2.Response.success(createMinimalForecastApi())
        }
        val expectedError = NotFoundError(message = "Location missing")
        val repository = DefaultWeatherRepository(
            networkDataSource = networkDataSource,
            locationRepository = FakeLocationRepository(Response.Failure(expectedError)),
            settingsRepository = FakeSettingsRepository(
                WeatherUnits(TemperatureUnit.Celsius, WindSpeedUnit.MetersPerSecond),
            ),
            apiMapper = WeatherApiMapper(),
            gson = Gson(),
        )

        val response = repository.getWeatherForecast()

        assertTrue(response is Response.Failure)
        assertFalse(networkDataSource.wasCalled)
        assertEquals(expectedError, (response as Response.Failure).data)
    }

    @Test
    fun `getWeatherForecast returns ConnectionError on IOException`() = runBlocking {
        val networkDataSource = FakeWeatherNetworkDataSource {
            throw IOException("Network unavailable")
        }
        val repository = DefaultWeatherRepository(
            networkDataSource = networkDataSource,
            locationRepository = FakeLocationRepository(
                Response.Success(LocationPoint(latitude = 43.2389, longitude = 76.8897)),
            ),
            settingsRepository = FakeSettingsRepository(
                WeatherUnits(TemperatureUnit.Celsius, WindSpeedUnit.MetersPerSecond),
            ),
            apiMapper = WeatherApiMapper(),
            gson = Gson(),
        )

        val response = repository.getWeatherForecast()

        assertTrue(response is Response.Failure)
        assertTrue((response as Response.Failure).data is ConnectionError)
    }

    @Test
    fun `getWeatherForecast returns ServerError on 500 HTTP response`() = runBlocking {
        val errorBody = """{"message":"Server error"}""".toResponseBody("application/json".toMediaType())
        val networkDataSource = FakeWeatherNetworkDataSource {
            retrofit2.Response.error(500, errorBody)
        }
        val repository = DefaultWeatherRepository(
            networkDataSource = networkDataSource,
            locationRepository = FakeLocationRepository(
                Response.Success(LocationPoint(latitude = 43.2389, longitude = 76.8897)),
            ),
            settingsRepository = FakeSettingsRepository(
                WeatherUnits(TemperatureUnit.Celsius, WindSpeedUnit.MetersPerSecond),
            ),
            apiMapper = WeatherApiMapper(),
            gson = Gson(),
        )

        val response = repository.getWeatherForecast()

        assertTrue(response is Response.Failure)
        assertTrue((response as Response.Failure).data is ServerError)
    }

    private fun createMinimalForecastApi(): WeatherForecastApi = WeatherForecastApi().apply {
        current = CurrentWeatherApi().apply {
            temperature2m = 57.0
            apparentTemperature = 55.0
            relativeHumidity2m = 50
            isDay = 1
            weatherCode = 0
            surfacePressure = 1013.0
            windSpeed10m = 15.0
            windDirection10m = 90.0
            windGusts10m = 20.0
            dewPoint2m = 41.0
            uvIndex = 3.0
        }
        daily = DailyWeatherApi().apply {
            time = listOf("2026-11-21")
            temperature2mMin = listOf(45.0)
            temperature2mMax = listOf(64.0)
            weatherCode = listOf(0)
        }
    }

    private class FakeLocationRepository(
        private val result: Response<LocationPoint>,
    ) : LocationRepository {
        override suspend fun getCurrentLocation(): Response<LocationPoint> = result
    }

    private class FakeSettingsRepository(
        private val units: WeatherUnits,
    ) : SettingsRepository {
        override fun getWeatherUnits(): WeatherUnits = units

        override suspend fun getTemperatureMetrics(): Response<List<TemperatureMetric>> =
            Response.Success(emptyList())

        override suspend fun saveSelectedTemperatureMetric(metric: TemperatureMetric): Response<Unit> =
            Response.Success(Unit)

        override suspend fun getWindSpeedMetrics(): Response<List<WindSpeedMetric>> =
            Response.Success(emptyList())

        override suspend fun saveSelectedWindSpeedMetric(metric: WindSpeedMetric): Response<Unit> =
            Response.Success(Unit)
    }

    private class FakeWeatherNetworkDataSource(
        private val handler: suspend () -> retrofit2.Response<WeatherForecastApi>,
    ) : WeatherNetworkDataSource {
        var wasCalled: Boolean = false
            private set
        var lastLatitude: Double? = null
            private set
        var lastLongitude: Double? = null
            private set
        var lastTemperatureUnit: TemperatureUnitApi? = null
            private set
        var lastWindSpeedUnit: WindSpeedUnitApi? = null
            private set

        override suspend fun getForecast(
            latitude: Double,
            longitude: Double,
            current: String,
            hourly: String,
            daily: String,
            temperatureUnit: TemperatureUnitApi,
            windSpeedUnit: WindSpeedUnitApi,
            timezone: String,
            forecastDays: Int,
        ): retrofit2.Response<WeatherForecastApi> {
            wasCalled = true
            lastLatitude = latitude
            lastLongitude = longitude
            lastTemperatureUnit = temperatureUnit
            lastWindSpeedUnit = windSpeedUnit
            return handler()
        }
    }
}
