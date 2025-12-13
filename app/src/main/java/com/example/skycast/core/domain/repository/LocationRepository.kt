package com.example.skycast.core.domain.repository

import com.example.skycast.core.domain.model.DeviceLocation

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<DeviceLocation>
}