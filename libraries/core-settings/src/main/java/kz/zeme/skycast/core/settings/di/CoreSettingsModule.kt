package kz.zeme.skycast.core.settings.di

import android.content.Context
import android.content.SharedPreferences
import kz.zeme.skycast.core.di.extensions.binds
import kz.zeme.skycast.core.settings.data.DefaultSettingsRepository
import kz.zeme.skycast.core.settings.data.SettingsLocalDataSource
import kz.zeme.skycast.core.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

val coreSettingsModule = module {
    single<SharedPreferences>(named(SETTINGS_PREFERENCES_NAME)) {
        androidContext().getSharedPreferences(SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    single {
        SettingsLocalDataSource(
            sharedPreferences = get(named(SETTINGS_PREFERENCES_NAME)),
        )
    }

    single {
        DefaultSettingsRepository(
            settingsLocalDataSource = get(),
        )
    }.binds(
        SettingsRepository::class,
    )
}
