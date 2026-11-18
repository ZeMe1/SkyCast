package kz.zeme.skycast.features.navigation.deeplink

import android.content.Intent
import android.os.Bundle
import kz.zeme.skycast.core.navigation.BaseNavigation
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.core.navigation.extensions.getRedirectOrNull

class AppGlobalRedirectParser : AppRedirectParser {

    override fun getRedirectOrNull(intent: Intent?): Navigator.Navigation? {
        intent ?: return null

        intent.getRedirectOrNull()?.let {
            if (it is BaseNavigation.ShowMain) return it
            return BaseNavigation.ShowMain(restart = false, redirect = it, withSplash = false)
        }

        intent.data?.let {
            return BaseNavigation.ShowDeeplink(url = it.toString())
        }

        return null
    }

    override fun getRedirectOrNull(bundle: Bundle?): Navigator.Navigation? =
        bundle?.getRedirectOrNull()
}
