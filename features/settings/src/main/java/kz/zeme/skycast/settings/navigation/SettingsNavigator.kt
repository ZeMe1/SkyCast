package kz.zeme.skycast.settings.navigation

import androidx.fragment.app.DialogFragment
import kz.zeme.skycast.core.activity.ActivityProvider
import kz.zeme.skycast.core.fragment.extensions.dismiss
import kz.zeme.skycast.core.fragment.extensions.isShown
import kz.zeme.skycast.core.navigation.BaseNavigation
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.core.navigation.Navigator

class SettingsNavigator(
    private val activityProvider: ActivityProvider,
    private val settingsRouter: SettingsRouter,
    private val externalModulesNavigator: ExternalModulesNavigator,
) : Navigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
        is SettingsNavigation.ShowUnitPicker -> showUnitPicker(navigation)
        else -> externalModulesNavigator.navigate(navigation)
    }

    private fun goBack() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val fragment = fm.fragments.lastOrNull()

        if (fragment is DialogFragment) fragment.dismiss()
        else fm.popBackStack()
    }

    private fun close() {
        activityProvider.currentActivity?.finish()
    }

    private fun showUnitPicker(navigation: SettingsNavigation.ShowUnitPicker) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = SettingsRouter.TAG_UNIT_PICKER

        if (fm.isShown(tag)) return

        fm.dismiss(tag)

        settingsRouter
            .getUnitPickerDialog(navigation.type)
            .show(fm, tag)
    }
}
