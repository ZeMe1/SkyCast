package kz.zeme.skycast.settings.navigation

import android.os.Bundle
import kz.zeme.skycast.core.fragment.BaseBottomSheetDialog
import kz.zeme.skycast.core.navigation.extensions.getTypedParcelable
import kz.zeme.skycast.settings.presentation.main.SettingsFragment
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerFragment
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerType

class SettingsRouter {

    companion object {
        const val TAG_SETTINGS = "TAG_SETTINGS"
        const val TAG_UNIT_PICKER = "TAG_UNIT_PICKER"

        private const val UNIT_PICKER_TYPE_KEY = "UNIT_PICKER_TYPE_KEY"
    }

    fun getSettingsFragment(): SettingsFragment = SettingsFragment()

    fun getUnitPickerDialog(type: UnitPickerType): BaseBottomSheetDialog =
        UnitPickerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(UNIT_PICKER_TYPE_KEY, type)
            }
        }

    fun getUnitPickerTypeOrNull(bundle: Bundle?): UnitPickerType? =
        bundle?.getTypedParcelable(UNIT_PICKER_TYPE_KEY)
}
