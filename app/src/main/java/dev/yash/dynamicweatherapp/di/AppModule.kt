package dev.yash.dynamicweatherapp.di

import androidx.room.Room
import dev.yash.dynamicweatherapp.data.local.WeatherDatabase
import dev.yash.dynamicweatherapp.data.local.dao.LocationDao
import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.yash.dynamicweatherapp.data.location.DefaultLocationTracker
import dev.yash.dynamicweatherapp.data.remote.OpenMeteoApi
import dev.yash.dynamicweatherapp.data.repository.WeatherRepositoryImpl
import dev.yash.dynamicweatherapp.domain.location.LocationTracker
import dev.yash.dynamicweatherapp.domain.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Helper function to check internet
    private fun isInternetAvailable(context: android.content.Context): Boolean {
        val connectivityManager = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: android.content.Context): OkHttpClient {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = Cache(File(context.cacheDir, "weather_cache"), cacheSize)

        val offlineInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!isInternetAvailable(context)) {
                val maxStale = 60 * 60 * 24 * 7 // Offline cache valid for 7 days
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(offlineInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(okHttpClient: OkHttpClient): OpenMeteoApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(okHttpClient) // <-- Passes the cache client
            .addConverterFactory(GsonConverterFactory.create())  // Using Gson!
            .build()
            .create(OpenMeteoApi::class.java)
    }


    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun provideLocationTracker(
        locationClient: FusedLocationProviderClient,
        app: Application
    ): LocationTracker {
        return DefaultLocationTracker(locationClient, app)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: OpenMeteoApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: android.content.Context): androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> {
        return androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings_prefs") }
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>): dev.yash.dynamicweatherapp.domain.settings.SettingsRepository {
        return dev.yash.dynamicweatherapp.data.repository.SettingsRepositoryImpl(dataStore)
    }
    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: android.content.Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(db: WeatherDatabase): LocationDao {
        return db.locationDao
    }
}