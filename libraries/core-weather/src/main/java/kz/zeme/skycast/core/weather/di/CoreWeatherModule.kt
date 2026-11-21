package kz.zeme.skycast.core.weather.di

import kz.zeme.skycast.core.di.extensions.binds
import kz.zeme.skycast.core.env.props.BaseSettings.Property
import kz.zeme.skycast.core.network.extensions.createWebService
import kz.zeme.skycast.core.weather.data.DefaultWeatherRepository
import kz.zeme.skycast.core.weather.data.WeatherApiMapper
import kz.zeme.skycast.core.weather.data.WeatherNetworkDataSource
import kz.zeme.skycast.core.weather.domain.WeatherRepository
import org.koin.dsl.module

val coreWeatherModule = module {
    single {
        createWebService<WeatherNetworkDataSource>(Property.baseUrl)
    }

    factory {
        WeatherApiMapper()
    }

    single {
        DefaultWeatherRepository(
            networkDataSource = get(),
            locationRepository = get(),
            settingsRepository = get(),
            apiMapper = get(),
            gson = get(),
        )
    }.binds(
        WeatherRepository::class,
    )
}
