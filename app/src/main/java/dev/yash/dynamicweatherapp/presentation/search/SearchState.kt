package dev.yash.dynamicweatherapp.presentation.search

import dev.yash.dynamicweatherapp.data.local.entity.SavedLocationEntity
import dev.yash.dynamicweatherapp.domain.model.LocationSearchResult
import dev.yash.dynamicweatherapp.domain.model.WeatherInfo
import dev.yash.dynamicweatherapp.domain.settings.TemperatureUnit

//A wrapper class to hold the DB location and its live weather data together
data class SavedLocationWeatherState(
    val location: SavedLocationEntity,
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

data class SearchState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<LocationSearchResult> = emptyList(),
    val savedLocations: List<SavedLocationWeatherState> = emptyList(),
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val error: String? = null
)