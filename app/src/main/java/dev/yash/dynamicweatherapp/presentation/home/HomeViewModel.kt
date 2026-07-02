package dev.yash.dynamicweatherapp.presentation.home

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.yash.dynamicweatherapp.domain.location.LocationTracker
import dev.yash.dynamicweatherapp.domain.repository.WeatherRepository
import dev.yash.dynamicweatherapp.domain.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context, // Inject context for Geocoder
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val passedLat: String? = savedStateHandle.get<String>("lat")
    private val passedLon: String? = savedStateHandle.get<String>("lon")
    private val passedCityName: String? = savedStateHandle.get<String>("cityName")

    init {
        //privacy policy check
        viewModelScope.launch {
            settingsRepository.hasAcceptedPrivacyPolicy.collect { accepted ->
                _state.update { it.copy(hasAcceptedPrivacyPolicy = accepted) }
                if (accepted && _state.value.weatherInfo == null) {
                    loadWeatherInfo()
                }
//                if (_state.value.weatherInfo == null) {
//                    loadWeatherInfo()
//                }
            }
        }

        // Observe temperature unit for the Home Screen
        viewModelScope.launch {
            settingsRepository.temperatureUnit.collect { unit ->
                _state.update { it.copy(temperatureUnit = unit) }
            }
        }
    }

    fun loadWeatherInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            if (passedLat != null && passedLon != null) {
                val cityName = passedCityName ?: "Selected Location"
                fetchWeatherForLocation(passedLat.toDouble(), passedLon.toDouble(), cityName)
            } else {

                locationTracker.getCurrentLocation()?.let { location ->
                    val detectedCity = getCityNameFromCoordinates(location.latitude, location.longitude)
                    fetchWeatherForLocation(location.latitude, location.longitude, detectedCity)
                } ?: run {
                    _state.update { it.copy(
                        isLoading = false,
                        error = "Unable to retrieve GPS location. Verify device permissions."
                    ) }
                }
            }
        }
    }

    private suspend fun fetchWeatherForLocation(lat: Double, lon: Double, locationName: String) {
        val result = repository.getWeatherData(lat, lon)
        result.fold(
            onSuccess = { weatherData ->
                _state.update { it.copy(
                    weatherInfo = weatherData,
                    isLoading = false,
                    error = null,
                    locationName = locationName,
                    lastSyncTime = System.currentTimeMillis()
                ) }
            },
            onFailure = { error ->
                _state.update { it.copy(
                    // Do NOT set weatherInfo = null here. Keep the old data!
                    isLoading = false,
                    error = "Internet is currently unavailable. Showing last sync weather data, please connect to internet and refresh."
                ) }
            }
        )
    }

    private suspend fun getCityNameFromCoordinates(lat: Double, lon: Double): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            addresses?.firstOrNull()?.locality ?: "Current Location"
        } catch (e: Exception) {
            "Current Location"
        }
    }
    fun acceptPrivacyPolicy() {
        viewModelScope.launch {

            settingsRepository.setPrivacyPolicyAccepted(true)
        }
    }

    fun forceRefresh() {
        // Prevent spam-clicking
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, error = null) }

        // Call whatever function you use to initially load the weather
        // (e.g., loadWeatherInfo(), fetchWeatherForLocation(), etc.)
        loadWeatherInfo()
    }
}