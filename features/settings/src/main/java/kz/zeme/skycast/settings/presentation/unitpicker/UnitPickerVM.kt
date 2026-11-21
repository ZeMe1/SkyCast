package kz.zeme.skycast.settings.presentation.unitpicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.navigation.BaseNavigation.Close
import kz.zeme.skycast.core.navigation.BaseNavigation.ShowMain
import kz.zeme.skycast.core.navigation.BaseNavigation.ShowToast
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.core.observable.EventFlow
import kz.zeme.skycast.core.observable.MutableEventFlow
import kz.zeme.skycast.core.observable.ViewState
import kz.zeme.skycast.core.observable.extensions.getErrorState
import kz.zeme.skycast.core.observable.extensions.getLoadingState
import kz.zeme.skycast.core.observable.extensions.getViewState
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.settings.domain.SettingsRepository

open class UnitPickerVM(
    private val unitPickerType: UnitPickerType,
    private val settingsRepository: SettingsRepository,
) : ViewModel(), UnitPickerController {

    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)
    private val _units = MutableLiveData<ViewState<List<UnitPickerItem>>>()

    private var getUnitsJob: Job? = null
    private var saveUnitJob: Job? = null

    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()
    val units: LiveData<ViewState<List<UnitPickerItem>>> = _units

    override fun onUnitClick(item: UnitPickerItem) = onSaveSelectedUnit(item)

    override fun onClose() {
        _navigation.value = Close
    }

    override fun onStart() = onLoadUnits()

    private fun onLoadUnits() {
        if (getUnitsJob?.isActive == true) return

        getUnitsJob = loadUnits()
    }

    private fun loadUnits() = viewModelScope.launch {
        _units.value = _units.getLoadingState()

        _units.value = when (unitPickerType) {
            UnitPickerType.Temperature -> _units.getViewState(
                response = settingsRepository.getTemperatureMetrics(),
                transform = { metrics -> metrics.map { it.toUnitPickerItem() } },
            )
            UnitPickerType.WindSpeed -> _units.getViewState(
                response = settingsRepository.getWindSpeedMetrics(),
                transform = { metrics -> metrics.map { it.toUnitPickerItem() } },
            )
        }
    }

    private fun onSaveSelectedUnit(item: UnitPickerItem) {
        if (saveUnitJob?.isActive == true) return

        saveUnitJob = saveUnit(item)
    }

    private fun saveUnit(item: UnitPickerItem) = viewModelScope.launch {
        _units.value = _units.getLoadingState()

        val response = when (unitPickerType) {
            UnitPickerType.Temperature -> settingsRepository.saveSelectedTemperatureMetric(
                item.toTemperatureMetric(),
            )
            UnitPickerType.WindSpeed -> settingsRepository.saveSelectedWindSpeedMetric(
                item.toWindSpeedMetric(),
            )
        }

        when (response) {
            is Response.Failure -> _units.value = _units.getErrorState(response)
            is Response.Success -> {
                _navigation.value = ShowToast.MessageRes(R.string.unit_picker_changed_message)
                _navigation.value = ShowMain(
                    restart = true,
                    withSplash = true,
                )
            }
        }
    }
}
