package kz.zeme.skycast.settings.presentation.unitpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.skycast.core.fragment.BaseBottomSheetDialog
import kz.zeme.skycast.core.fragment.extensions.composeView
import kz.zeme.skycast.core.navigation.BaseNavigation
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.settings.navigation.SettingsRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UnitPickerFragment : BaseBottomSheetDialog() {

    private val settingsRouter: SettingsRouter by inject()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()

    private val unitPickerType: UnitPickerType
        get() = settingsRouter.getUnitPickerTypeOrNull(arguments)!!

    private val unitPickerVM: UnitPickerVM by viewModel {
        parametersOf(unitPickerType)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        UnitPickerScreen(
            unitPickerType = unitPickerType,
            state = unitPickerVM.units.observeAsState(),
            controller = unitPickerVM,
        )
    }.also {
        unitPickerVM.onStart()
    }

    override fun startObserving() {
        unitPickerVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = ::navigate,
        )
    }

    private fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack,
        is BaseNavigation.Close -> dismiss()
        else -> externalModulesNavigator.navigate(navigation)
    }
}
