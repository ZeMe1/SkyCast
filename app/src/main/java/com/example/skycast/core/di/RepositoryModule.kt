package com.example.skycast.core.di

import com.example.skycast.core.data.repository.WeatherRepositoryImpl
import com.example.skycast.core.data.repository.LocationRepositoryImpl
import com.example.skycast.core.domain.repository.LocationRepository
import com.example.skycast.core.domain.repository.WeatherRepository
import org.koin.dsl.module


val repositoryModule = module {


    single<WeatherRepository> {
        WeatherRepositoryImpl(
            apiService = get(),
            apiKey = "7ed8a64baadb47f8b5d60126250812"
        )
    }

    single<LocationRepository> {
        LocationRepositoryImpl(
            context = get(),
            fusedClient = get()
        )
    }
}