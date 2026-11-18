package kz.zeme.skycast

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import kz.zeme.skycast.di.KoinController
import kz.zeme.skycast.di.KoinModules
import org.koin.android.ext.android.get

open class App : Application() {

    open val koinModules: KoinModules = KoinModules()

    override fun onCreate() {
        super.onCreate()

        KoinController.onStart(
            application = this,
            modules = koinModules.getModules(),
        )

        registerActivityLifecycleCallbacks(get<ActivityLifecycleCallbacks>())
    }
}
