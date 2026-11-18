package kz.zeme.skycast.core.navigation.di

import kz.zeme.skycast.core.navigation.NavigationResultLiveData
import org.koin.dsl.module

val coreNavigationModule = module {
    single { NavigationResultLiveData() }
}
