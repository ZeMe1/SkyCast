package com.example.skycast.core.data.retrofit

import com.example.skycast.core.data.retrofit.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {

    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 7
    ): WeatherResponseDto
}


