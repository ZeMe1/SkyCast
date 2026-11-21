package kz.zeme.skycast.features.navigation

import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.commit
import kz.zeme.skycast.MainActivity
import kz.zeme.skycast.core.activity.ActivityProvider
import kz.zeme.skycast.core.activity.extensions.makeToast
import kz.zeme.skycast.core.fragment.extensions.isShown
import kz.zeme.skycast.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.skycast.core.locale.view.presentation.navigation.LanguagePickerRouter
import kz.zeme.skycast.core.navigation.BaseNavigation
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.core.navigation.extensions.putRedirect
import kz.zeme.skycast.core.navigation.extensions.setSlideHorizontalAnimations
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.home.navigation.HomeExternalNavigation
import kz.zeme.skycast.home.navigation.HomeRouter
import kz.zeme.skycast.settings.navigation.SettingsExternalNavigation
import kz.zeme.skycast.settings.navigation.SettingsRouter
import kz.zeme.skycast.splash.SplashExternalNavigation
import kz.zeme.skycast.splash.SplashFragment

class AppGlobalNavigator(
    private val activityProvider: ActivityProvider,
    private val homeRouter: HomeRouter,
    private val settingsRouter: SettingsRouter,
    private val languagePickerRouter: LanguagePickerRouter,
) : ExternalModulesNavigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.ShowMain -> showMain(navigation)
        is SplashExternalNavigation.ShowNextScreen -> showSplashNextScreen(navigation)
        is HomeExternalNavigation.OpenSettings,
        is SettingsExternalNavigation.OpenSettings -> openSettings()
        is LanguagePickerNavigation.OpenLanguagePicker -> openLanguagePicker()
        is BaseNavigation.ShowToast.Message -> showToastMessage(navigation)
        is BaseNavigation.ShowToast.MessageErr -> showToastMessageErr(navigation)
        is BaseNavigation.ShowToast.MessageRes -> showToastMessageRes(navigation)
        is BaseNavigation.ShowDeeplink -> showDeeplink(navigation)
        else -> Unit
    }

    private fun showMain(navigation: BaseNavigation.ShowMain) {
        val activity = activityProvider.currentActivity ?: return

        if (navigation.restart) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putRedirect(navigation.redirect)
            }
            activity.finish()
            activity.startActivity(intent)
            return
        }

        val fm = activity.supportFragmentManager

        if (navigation.withSplash) {
            val hasSplash = fm.fragments
                .filterIsInstance<SplashFragment>()
                .isNotEmpty()

            if (hasSplash) return

            fm.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, SplashFragment())
            }
            return
        }

        val tag = HomeRouter.TAG_HOME
        val existingFragment = fm.findFragmentByTag(tag)

        if (existingFragment == null) {
            fm.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.fragment_container_view,
                    homeRouter.getHomeFragment(),
                    tag,
                )
            }
        }

        navigation.redirect?.let { redirect ->
            navigate(redirect)
        }
    }

    private fun showSplashNextScreen(navigation: SplashExternalNavigation.ShowNextScreen) {
        val redirect = (navigation.redirect as? BaseNavigation.ShowMain)?.redirect ?: navigation.redirect
        showMain(
            BaseNavigation.ShowMain(
                restart = false,
                redirect = redirect,
                withSplash = false,
            ),
        )
    }

    private fun openSettings() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = SettingsRouter.TAG_SETTINGS

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                settingsRouter.getSettingsFragment(),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun openLanguagePicker() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = LanguagePickerRouter.TAG_LANGUAGE_PICKER

        if (fm.isShown(tag)) return

        languagePickerRouter
            .getLanguagePickerFragment()
            .show(fm, tag)
    }

    private fun showToastMessage(navigation: BaseNavigation.ShowToast.Message) {
        val context = activityProvider.currentActivity ?: return
        context.makeToast(navigation.msg)
    }

    private fun showToastMessageErr(navigation: BaseNavigation.ShowToast.MessageErr) {
        val context = activityProvider.currentActivity ?: return
        context.makeToast(navigation.error)
    }

    private fun showToastMessageRes(navigation: BaseNavigation.ShowToast.MessageRes) {
        val context = activityProvider.currentActivity ?: return
        context.makeToast(navigation.msgRes)
    }

    private fun showDeeplink(navigation: BaseNavigation.ShowDeeplink) {
        val context = activityProvider.currentActivity ?: return
        context.startActivity(Intent(Intent.ACTION_VIEW, navigation.url.toUri()))
    }
}
