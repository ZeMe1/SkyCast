package kz.zeme.skycast.core.network.interceptors

import android.os.Build
import kz.zeme.skycast.core.network.Headers
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.header(Headers.UserAgent) != null) return chain.proceed(original)

        val request = original.newBuilder()
            .header(Headers.UserAgent, "Android/${Build.VERSION.RELEASE} (${Build.MODEL})")
            .build()

        return chain.proceed(request)
    }
}
