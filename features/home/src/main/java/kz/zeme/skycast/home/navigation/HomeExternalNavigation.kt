package kz.zeme.skycast.home.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.skycast.core.navigation.Navigator

@Parcelize
sealed class HomeExternalNavigation : Navigator.Navigation {
    data object OpenSettings : HomeExternalNavigation()
}
