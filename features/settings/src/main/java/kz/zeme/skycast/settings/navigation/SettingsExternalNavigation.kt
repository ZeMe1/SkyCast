package kz.zeme.skycast.settings.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.skycast.core.navigation.Navigator

@Parcelize
sealed class SettingsExternalNavigation : Navigator.Navigation {
    data object OpenSettings : SettingsExternalNavigation()
}
