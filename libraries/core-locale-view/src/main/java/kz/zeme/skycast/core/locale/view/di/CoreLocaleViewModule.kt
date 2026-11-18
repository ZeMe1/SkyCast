package kz.zeme.skycast.core.locale.view.di

import kz.zeme.skycast.core.locale.view.presentation.LanguagePickerVM
import kz.zeme.skycast.core.locale.view.presentation.navigation.LanguagePickerRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreLocaleViewModule = module {
    factory { LanguagePickerRouter() }

    viewModel {
        LanguagePickerVM(
            languageRepository = get(),
        )
    }
}
