package kz.zeme.skycast.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.zeme.skycast.core.fragment.BaseFragment
import kz.zeme.skycast.core.fragment.extensions.composeView
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.features.navigation.deeplink.AppRedirectParser
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment() {

    private val appRedirectParser: AppRedirectParser by inject()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        SplashScreen(
            onSplashFinished = ::navigateNext,
        )
    }

    private fun navigateNext() {
        val redirect = appRedirectParser.getRedirectOrNull(activity?.intent)
        externalModulesNavigator.navigate(
            SplashExternalNavigation.ShowNextScreen(
                redirect = redirect,
            ),
        )
    }
}
