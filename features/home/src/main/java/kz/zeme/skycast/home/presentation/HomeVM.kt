package kz.zeme.skycast.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.skycast.core.navigation.Navigator
import kz.zeme.skycast.core.observable.EventFlow
import kz.zeme.skycast.core.observable.MutableEventFlow
import kz.zeme.skycast.core.observable.ViewState
import kz.zeme.skycast.core.observable.extensions.getLoadingState
import kz.zeme.skycast.core.observable.extensions.getResponse
import kz.zeme.skycast.core.weather.domain.WeatherForecast
import kz.zeme.skycast.core.weather.domain.WeatherRepository
import kz.zeme.skycast.home.navigation.HomeExternalNavigation

class HomeVM(
    private val weatherRepository: WeatherRepository,
) : ViewModel(), HomeController {

    private val _weatherState = MutableLiveData<ViewState<WeatherForecast>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var loadWeatherJob: Job? = null

    val weatherState: LiveData<ViewState<WeatherForecast>> = _weatherState
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onStart() {
        if (loadWeatherJob?.isActive == true) return

        loadWeatherJob = loadWeather()
    }

    override fun onRefresh() {
        loadWeatherJob?.cancel()

        _weatherState.value = _weatherState.getLoadingState()
        loadWeatherJob = loadWeather()
    }

    override fun onSettingsClick() {
        _navigation.value = HomeExternalNavigation.OpenSettings
    }

    private fun loadWeather() = viewModelScope.launch {
        _weatherState.getResponse(weatherRepository.getWeatherForecast())
    }
}
