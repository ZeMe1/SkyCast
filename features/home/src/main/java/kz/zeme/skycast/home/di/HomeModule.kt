package kz.zeme.skycast.home.di

import kz.zeme.skycast.home.navigation.HomeNavigator
import kz.zeme.skycast.home.navigation.HomeRouter
import kz.zeme.skycast.home.presentation.HomeVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    factory {
        HomeRouter()
    }

    factory {
        HomeNavigator(
            activityProvider = get(),
            externalModulesNavigator = get(),
        )
    }

    viewModel {
        HomeVM(
            weatherRepository = get(),
        )
    }
}
