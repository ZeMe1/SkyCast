package kz.zeme.skycast.settings.presentation.main

interface SettingsController {
    fun onStart()
    fun onBackPressed()
    fun onTemperatureUnitClick()
    fun onWindSpeedUnitClick()
    fun onLanguageClick()
}
