package kz.zeme.skycast.settings.presentation.unitpicker

interface UnitPickerController {
    fun onUnitClick(item: UnitPickerItem)
    fun onClose()
    fun onStart()
}
