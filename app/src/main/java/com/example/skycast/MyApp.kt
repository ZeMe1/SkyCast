package com.example.skycast

import android.app.Application
import com.example.skycast.core.di.apiModule
import com.example.skycast.core.di.repositoryModule
import com.example.skycast.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(
                apiModule,           // Provides Retrofit and API services
                repositoryModule,    // Provides repository implementations
                viewModelModule      // Provides ViewModels
            )
        }
    }
}