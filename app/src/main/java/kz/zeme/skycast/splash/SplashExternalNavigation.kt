package kz.zeme.skycast.splash

import kotlinx.parcelize.Parcelize
import kz.zeme.skycast.core.navigation.Navigator

@Parcelize
sealed class SplashExternalNavigation : Navigator.Navigation {

    data class ShowNextScreen(
        val redirect: Navigator.Navigation? = null,
    ) : SplashExternalNavigation()
}
