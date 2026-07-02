package dev.yash.dynamicweatherapp.presentation.home

import dev.yash.dynamicweatherapp.domain.model.WeatherInfo
import dev.yash.dynamicweatherapp.domain.settings.TemperatureUnit


data class HomeState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val error: String? = null,
    val locationName: String = "Locating...",
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val hasAcceptedPrivacyPolicy: Boolean? = null,
    val lastSyncTime: Long? = null
)