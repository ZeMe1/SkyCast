package com.example.skycast.core.domain.model

import com.example.skycast.R

data class DailyForecast(
    val dayOfWeek: String,
    val skyForecast: String,
    val maxTemp: String,
    val minTemp: String,
    val icon: Int
)