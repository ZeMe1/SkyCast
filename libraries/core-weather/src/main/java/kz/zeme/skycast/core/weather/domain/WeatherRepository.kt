package kz.zeme.skycast.core.weather.domain

import kz.zeme.skycast.core.domain.Response

interface WeatherRepository {

    suspend fun getWeatherForecast(): Response<WeatherForecast>
}
