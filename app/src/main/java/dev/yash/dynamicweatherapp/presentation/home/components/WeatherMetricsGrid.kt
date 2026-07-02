package dev.yash.dynamicweatherapp.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.yash.dynamicweatherapp.R
import dev.yash.dynamicweatherapp.domain.model.CurrentWeather
import dev.yash.dynamicweatherapp.presentation.common.GlassCard
import dev.yash.dynamicweatherapp.presentation.theme.NimbusAccentBlue
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextHint
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextWhite

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import dev.yash.dynamicweatherapp.presentation.common.shimmerEffect

@Composable
fun WeatherMetricsGrid(
    currentWeather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Row 1: Humidity & Wind
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "HUMIDITY",
                value = "${currentWeather.humidity}%",
                subtitle = "Moderate",
                iconRes = R.drawable.ic_humidity // Pointing to your custom SVG!
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "WIND",
                value = "${currentWeather.windSpeed} km/h",
                subtitle = "Steady",
                iconRes = R.drawable.ic_wind
            )
        }

        // Row 2: Sunrise & Sunset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "SUNRISE",
                value = "5:36 am", // Replace with real data if you have it in your model
                subtitle = "Morning",
                iconRes = R.drawable.ic_sunrise
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "SUNSET",
                value = "7:09 pm", // Replace with real data if you have it in your model
                subtitle = "Evening",
                iconRes = R.drawable.ic_sunset
            )
        }

        // Row 3: UV Index & Pressure
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "UV INDEX",
                value = "8.55", // Replace with real data if you have it in your model
                subtitle = "SPF recommended",
                iconRes = R.drawable.ic_uv_index
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "PRESSURE",
                value = "${currentWeather.pressure} hPa",
                subtitle = "Steady",
                iconRes = R.drawable.ic_pressure
            )
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    iconRes: Int
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = NimbusTextHint
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = NimbusTextWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = NimbusTextHint
            )
        }
    }
}

@Composable
fun WeatherMetricsGridSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3) { // 3 Rows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
                // Right Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}