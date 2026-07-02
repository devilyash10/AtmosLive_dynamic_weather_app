package dev.yash.dynamicweatherapp.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.yash.dynamicweatherapp.presentation.theme.NimbusAccentBlue
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextHint
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextWhite
import kotlinx.coroutines.delay
import androidx.compose.runtime.*

@Composable
fun LocationHeader(
    locationName: String,
    lastSyncTime: Long?,
    onRefreshClick: () -> Unit
) {
    // 1. Create a State variable for the current time
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // 2. Create a "Ticker" that updates the time every 60 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L) // Wait 1 minute
            currentTime = System.currentTimeMillis() // This triggers the UI to refresh!
        }
    }

    // 3. Calculate using the ticking `currentTime` instead of a static call
    val syncText = if (lastSyncTime != null) {
        val diffMinutes = (currentTime - lastSyncTime) / (1000 * 60)
        when {
            diffMinutes < 1 -> "Just now"
            diffMinutes < 60 -> "${diffMinutes}m ago"
            else -> "${diffMinutes / 60}h ago"
        }
    } else {
        "Updating..."
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Location Name Left Side
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = NimbusAccentBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = locationName.ifBlank { "Locating..." },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                ),
                color = NimbusTextWhite
            )
        }

        // Smart Refresh Button Right Side
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { onRefreshClick() }
                .padding(4.dp) // padding for larger touch target
        ) {
            Text(
                text = syncText,
                style = MaterialTheme.typography.labelMedium,
                color = NimbusTextHint
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Refresh, // Adds a sleek refresh icon next to the text
                contentDescription = "Refresh Weather",
                tint = NimbusTextHint,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}