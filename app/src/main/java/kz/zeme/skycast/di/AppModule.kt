package kz.zeme.skycast.di

import android.app.Application.ActivityLifecycleCallbacks
import kz.zeme.skycast.core.activity.ActivityProvider
import kz.zeme.skycast.core.di.extensions.binds
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.features.AppGlobalActivityProvider
import kz.zeme.skycast.features.main.MainActivityVM
import kz.zeme.skycast.features.navigation.AppGlobalNavigator
import kz.zeme.skycast.features.navigation.deeplink.AppGlobalRedirectParser
import kz.zeme.skycast.features.navigation.deeplink.AppRedirectParser
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        AppGlobalActivityProvider()
    }.binds(
        ActivityProvider::class,
        ActivityLifecycleCallbacks::class,
    )

    factory {
        AppGlobalRedirectParser()
    }.binds(
        AppRedirectParser::class,
    )

    viewModel {
        MainActivityVM(
            appRedirectParser = get(),
        )
    }

    single {
        AppGlobalNavigator(
            activityProvider = get(),
            homeRouter = get(),
            settingsRouter = get(),
            languagePickerRouter = get(),
        )
    }.binds(
        ExternalModulesNavigator::class,
    )
}
