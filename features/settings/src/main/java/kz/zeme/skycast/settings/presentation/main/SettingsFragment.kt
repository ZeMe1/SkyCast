package kz.zeme.skycast.settings.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.skycast.core.fragment.BaseFragment
import kz.zeme.skycast.core.fragment.extensions.composeView
import kz.zeme.skycast.settings.navigation.SettingsNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {

    private val settingsVM: SettingsVM by viewModel()
    private val settingsNavigator: SettingsNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        SettingsScreen(
            state = settingsVM.settingsState.observeAsState(),
            controller = settingsVM,
        )
    }.also {
        settingsVM.onStart()
    }

    override fun startObserving() {
        settingsVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = settingsNavigator::navigate,
        )
    }
}
