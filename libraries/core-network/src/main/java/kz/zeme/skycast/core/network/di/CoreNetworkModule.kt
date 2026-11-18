package kz.zeme.skycast.core.network.di

import kz.zeme.skycast.core.network.extensions.createGson
import kz.zeme.skycast.core.network.extensions.createOkHttpClient
import kz.zeme.skycast.core.network.interceptors.DeviceDataFactory
import kz.zeme.skycast.core.network.interceptors.DeviceInfoInterceptor
import kz.zeme.skycast.core.network.interceptors.HttpErrorInterceptor
import kz.zeme.skycast.core.network.interceptors.UserAgentInterceptor
import org.koin.dsl.module

val coreNetworkModule = module {
    single { DeviceDataFactory() }
    single { DeviceInfoInterceptor(deviceDataFactory = get()) }
    single { UserAgentInterceptor() }
    single { HttpErrorInterceptor() }

    single {
        createOkHttpClient(
            connectTimeoutSec = 3L,
            readTimeoutSec = 10L,
            writeTimeOutSec = 60L,
            userAgentInterceptor = get(),
            deviceInfoInterceptor = get(),
            httpErrorInterceptor = get(),
        )
    }

    single { createGson() }
}
