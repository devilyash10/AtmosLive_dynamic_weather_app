package dev.yash.dynamicweatherapp.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.*
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.firstOrNull
import dev.yash.dynamicweatherapp.MainActivity
import kotlin.math.roundToInt

class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WidgetEntryPoint::class.java)
        val locationDao = entryPoint.locationDao()
        val weatherRepo = entryPoint.weatherRepository()

        val savedLocations = locationDao.getSavedLocations().firstOrNull()
        val primaryLocation = savedLocations?.firstOrNull()

        var tempString = "--°"
        var conditionString = "Tap to open app"

        if (primaryLocation != null) {
            // Fetch the weather for the primary location using your repository
            // Note: Update "getWeather" to match the exact method name in your WeatherRepository!
            val weatherResult = weatherRepo.getWeatherData(
                lat = primaryLocation.latitude,
                long = primaryLocation.longitude
            )

            // Extract data if the network/cache call was successful
            weatherResult.onSuccess { weatherInfo ->
                tempString = "${weatherInfo.current.temperature.roundToInt()}°"
                conditionString = weatherInfo.current.condition
            }
        }

        provideContent {
            if (primaryLocation == null) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ColorProvider(Color(0xCC121212L), night = Color(0xFF90CAF9)))
                        .padding(8.dp)
                        .clickable(actionStartActivity<MainActivity>()), // Opens app
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Open AtmosLive to add a city.",
                        style = TextStyle(color = ColorProvider(Color.White, night = Color(0xFF90CAF9)), fontSize = 14.sp)
                    )
                }
            } else {
                WidgetLayout(
                    cityName = primaryLocation.name,
                    temp = tempString,
                    desc = conditionString
                )
            }
        }
    }
}

@Composable
private fun WidgetLayout(cityName: String, temp: String, desc: String) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(day = Color(0xCC121212L), night = Color(0xCC121212L)))
            .padding(12.dp)
            .clickable(actionStartActivity<MainActivity>()), // Opens app when clicking layout
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = temp,
                style = TextStyle(
                    color = ColorProvider(day = Color.White, night = Color.White),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.width(12.dp))
            Column(
                modifier = GlanceModifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = cityName,
                    style = TextStyle(
                        color = ColorProvider(day = Color.White, night = Color.White),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = desc,
                    style = TextStyle(
                        color = ColorProvider(day = Color(0xFF90CAF9), night = Color(0xFF90CAF9)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}