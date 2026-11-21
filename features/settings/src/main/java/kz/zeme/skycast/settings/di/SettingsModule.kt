package kz.zeme.skycast.settings.di

import kz.zeme.skycast.settings.navigation.SettingsNavigator
import kz.zeme.skycast.settings.navigation.SettingsRouter
import kz.zeme.skycast.settings.presentation.main.SettingsVM
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerType
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory {
        SettingsRouter()
    }

    factory {
        SettingsNavigator(
            activityProvider = get(),
            settingsRouter = get(),
            externalModulesNavigator = get(),
        )
    }

    viewModel {
        SettingsVM(
            settingsRepository = get(),
            localeRepository = get(),
        )
    }

    viewModel { parameters ->
        UnitPickerVM(
            unitPickerType = parameters.get<UnitPickerType>(),
            settingsRepository = get(),
        )
    }
}
