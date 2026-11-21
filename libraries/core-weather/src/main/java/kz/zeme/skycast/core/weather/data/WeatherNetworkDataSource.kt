package kz.zeme.skycast.core.weather.data

import kz.zeme.skycast.core.weather.data.WeatherForecastApi.TemperatureUnitApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.WindSpeedUnitApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherNetworkDataSource {

    companion object {
        const val CURRENT_PARAMS =
            "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m,dew_point_2m,uv_index"
        const val HOURLY_PARAMS =
            "temperature_2m,precipitation_probability,weather_code,is_day"
        const val DAILY_PARAMS =
            "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max"
        const val AUTO_TIMEZONE = "auto"
        const val DEFAULT_FORECAST_DAYS = 7
    }

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = CURRENT_PARAMS,
        @Query("hourly") hourly: String = HOURLY_PARAMS,
        @Query("daily") daily: String = DAILY_PARAMS,
        @Query("temperature_unit") temperatureUnit: TemperatureUnitApi,
        @Query("wind_speed_unit") windSpeedUnit: WindSpeedUnitApi,
        @Query("timezone") timezone: String = AUTO_TIMEZONE,
        @Query("forecast_days") forecastDays: Int = DEFAULT_FORECAST_DAYS,
    ): Response<WeatherForecastApi>
}
