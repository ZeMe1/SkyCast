package com.example.skycast.core.di

import com.example.skycast.core.domain.repository.WeatherRepository
import com.example.skycast.core.presentation.main_screen.MainViewModel
import com.example.skycast.core.presentation.search_screen.SearchViewModel
import com.example.skycast.core.presentation.splash_screen.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        MainViewModel(
            weatherRepository = get<WeatherRepository>(),
            locationRepository = get()
        )
    }
    viewModel {
        SplashViewModel()
    }
    viewModel {
        SearchViewModel(get())
    }
}