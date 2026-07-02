package dev.yash.dynamicweatherapp.presentation.widget

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.yash.dynamicweatherapp.data.local.dao.LocationDao
import dev.yash.dynamicweatherapp.domain.repository.WeatherRepository
import dev.yash.dynamicweatherapp.domain.settings.SettingsRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun locationDao(): LocationDao
    fun weatherRepository(): WeatherRepository
    fun settingsRepository(): SettingsRepository
}