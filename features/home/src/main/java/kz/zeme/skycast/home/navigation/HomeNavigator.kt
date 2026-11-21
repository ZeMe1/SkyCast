package kz.zeme.skycast.home.navigation

import androidx.fragment.app.DialogFragment
import kz.zeme.skycast.core.activity.ActivityProvider
import kz.zeme.skycast.core.navigation.BaseNavigation
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.core.navigation.Navigator

class HomeNavigator(
    private val activityProvider: ActivityProvider,
    private val externalModulesNavigator: ExternalModulesNavigator,
) : Navigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
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
}
