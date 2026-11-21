package kz.zeme.skycast.settings.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerType

@Parcelize
sealed class SettingsNavigation : Navigator.Navigation {
    data class ShowUnitPicker(val type: UnitPickerType) : SettingsNavigation()
}
