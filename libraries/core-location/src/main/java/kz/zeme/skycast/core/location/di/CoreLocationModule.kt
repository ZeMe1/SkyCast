package kz.zeme.skycast.core.location.di

import android.content.Context
import android.location.LocationManager
import kz.zeme.skycast.core.di.extensions.binds
import kz.zeme.skycast.core.location.data.DefaultLocationRepository
import kz.zeme.skycast.core.location.domain.LocationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreLocationModule = module {
    single {
        androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    single {
        DefaultLocationRepository(
            activityProvider = get(),
            locationManager = get(),
        )
    }.binds(
        LocationRepository::class,
    )
}
