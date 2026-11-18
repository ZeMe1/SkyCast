package kz.zeme.skycast.di

import kz.zeme.skycast.core.locale.di.coreLocaleModule
import kz.zeme.skycast.core.locale.view.di.coreLocaleViewModule
import kz.zeme.skycast.core.navigation.di.coreNavigationModule
import kz.zeme.skycast.core.network.di.coreNetworkModule
import kz.zeme.skycast.core.ui.di.coreUiModule
import org.koin.core.module.Module

open class KoinModules {

    open fun getModules(): List<Module> = listOf(
        coreNetworkModule,
        coreNavigationModule,
        coreUiModule,
        coreLocaleModule,
        coreLocaleViewModule,
        appModule,
    )
}
