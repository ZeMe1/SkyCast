package kz.zeme.skycast.core.network.interceptors

import kz.zeme.skycast.core.network.Headers
import okhttp3.Interceptor
import okhttp3.Response

class DeviceInfoInterceptor(
    private val deviceDataFactory: DeviceDataFactory,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(Headers.XPlatform, deviceDataFactory.platform)
            .header(Headers.XDeviceModel, deviceDataFactory.deviceModel)
            .header(Headers.XDeviceOs, deviceDataFactory.deviceOs)
            .build()

        return chain.proceed(request)
    }
}
