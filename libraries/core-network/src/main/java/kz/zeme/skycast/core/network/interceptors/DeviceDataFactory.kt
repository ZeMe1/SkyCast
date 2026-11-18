package kz.zeme.skycast.core.network.interceptors

import android.os.Build

class DeviceDataFactory {
    val platform: String = "android"
    val deviceModel: String = Build.MODEL.orEmpty()
    val deviceOs: String = Build.VERSION.RELEASE.orEmpty()
    val sdkInt: Int = Build.VERSION.SDK_INT
}
