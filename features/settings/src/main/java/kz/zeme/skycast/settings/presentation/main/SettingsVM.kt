package kz.zeme.skycast.settings.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode
import kz.zeme.skycast.core.locale.domain.LocaleRepository
import kz.zeme.skycast.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.skycast.core.navigation.BaseNavigation
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.core.observable.EventFlow
import kz.zeme.skycast.core.observable.MutableEventFlow
import kz.zeme.skycast.core.observable.ViewState
import kz.zeme.skycast.core.observable.extensions.getLoadingState
import kz.zeme.skycast.core.settings.domain.SettingsRepository
import kz.zeme.skycast.settings.navigation.SettingsNavigation
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerType

class SettingsVM(
    private val settingsRepository: SettingsRepository,
    private val localeRepository: LocaleRepository,
) : ViewModel(), SettingsController {

    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)
    private val _settingsState = MutableLiveData<ViewState<SettingsState>>()

    private var loadSettingsJob: Job? = null

    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()
    val settingsState: LiveData<ViewState<SettingsState>> = _settingsState

    override fun onStart() = onLoadSettings()

    override fun onBackPressed() {
        _navigation.value = BaseNavigation.GoBack
    }

    override fun onTemperatureUnitClick() {
        _navigation.value = SettingsNavigation.ShowUnitPicker(UnitPickerType.Temperature)
    }

    override fun onWindSpeedUnitClick() {
        _navigation.value = SettingsNavigation.ShowUnitPicker(UnitPickerType.WindSpeed)
    }

    override fun onLanguageClick() {
        _navigation.value = LanguagePickerNavigation.OpenLanguagePicker
    }

    private fun onLoadSettings() {
        if (loadSettingsJob?.isActive == true) return

        loadSettingsJob = loadSettings()
    }

    private fun loadSettings() = viewModelScope.launch {
        _settingsState.value = _settingsState.getLoadingState()

        val units = settingsRepository.getWeatherUnits()
        val languageCode = LanguageCode.find(
            code = localeRepository.getSelectedLocale().language,
            default = LanguageCode.Ru,
        )

        _settingsState.value = ViewState.Data(
            SettingsState(
                temperatureUnit = units.temperatureUnit,
                windSpeedUnit = units.windSpeedUnit,
                languageCode = languageCode,
            ),
        )
    }
}
