package dev.yash.dynamicweatherapp.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.yash.dynamicweatherapp.domain.settings.TemperatureUnit
import dev.yash.dynamicweatherapp.presentation.common.GlassCard
import dev.yash.dynamicweatherapp.presentation.settings.components.AboutDeveloperDialog
import dev.yash.dynamicweatherapp.presentation.settings.components.PrivacyPolicyDialog
import dev.yash.dynamicweatherapp.presentation.theme.NimbusAccentBlue
import dev.yash.dynamicweatherapp.presentation.theme.NimbusDark
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextHint
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextWhite

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val temperatureUnit by viewModel.temperatureUnit.collectAsState()
    val syncInterval by viewModel.syncInterval.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Local state for dialogs and future toggles
    var showDeveloperDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    val showUnavailableToast = {
        Toast.makeText(context, "Service currently unavailable", Toast.LENGTH_SHORT).show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NimbusDark)
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = NimbusTextWhite
            )
            Text(
                text = "Customize your experience",
                style = MaterialTheme.typography.bodyMedium,
                color = NimbusAccentBlue
            )

            Spacer(modifier = Modifier.height(28.dp))

            // SECTION 1: PREFERENCES (Temperature)
            GlassCard(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = null,
                        tint = NimbusAccentBlue,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Temperature Unit",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NimbusTextWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (temperatureUnit == TemperatureUnit.CELSIUS) "Celsius (°C)" else "Fahrenheit (°F)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NimbusTextHint
                        )
                    }

                    Switch(
                        checked = temperatureUnit == TemperatureUnit.FAHRENHEIT,
                        onCheckedChange = { viewModel.toggleTemperatureUnit() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NimbusDark,
                            checkedTrackColor = NimbusAccentBlue,
                            uncheckedThumbColor = NimbusTextHint,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: BACKGROUND SYNC
            GlassCard(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = NimbusAccentBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Background Sync",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NimbusTextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    SyncOptionRow(title = "Never (On app open only)", selected = syncInterval == 0L) {
                        viewModel.updateSyncInterval(context, 0L)
                    }
                    SyncOptionRow(title = "Every 15 minutes", selected = syncInterval == 15L) {
                        viewModel.updateSyncInterval(context, 15L)
                    }
                    SyncOptionRow(title = "Every 30 minutes", selected = syncInterval == 30L) {
                        viewModel.updateSyncInterval(context, 30L)
                    }
                    SyncOptionRow(title = "Every 1 hour", selected = syncInterval == 60L) {
                        viewModel.updateSyncInterval(context, 60L)
                    }
                    SyncOptionRow(title = "Every 6 hours", selected = syncInterval == 360L) {
                        viewModel.updateSyncInterval(context, 360L)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 3: ABOUT & DEVELOPER
            GlassCard(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = NimbusAccentBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NimbusTextWhite
                        )
                    }

                    SettingsInfoRow(title = "Version", value = "2.6.0")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 20.dp))

                    SettingsInfoRow(title = "Data Source", value = "Open-Meteo API")
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 20.dp))

                    SettingsActionRow(title = "Rate your experience", onClick = showUnavailableToast)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 20.dp))

                    SettingsActionRow(title = "Privacy Policy", onClick = { showPrivacyPolicyDialog = true })
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 20.dp))

                    SettingsActionRow(title = "About the Developer", onClick = { showDeveloperDialog = true })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer branding
            Text(
                text = "Weather data powered by Open-Meteo API\n© 2026 AtmosLive Weather",
                style = MaterialTheme.typography.labelMedium,
                color = NimbusTextHint.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(120.dp)) // Extra padding for BottomNavBar
        }

        // Dialogs
        if (showPrivacyPolicyDialog) {
            PrivacyPolicyDialog(onDismiss = { showPrivacyPolicyDialog = false })
        }

        if (showDeveloperDialog) {
            AboutDeveloperDialog(onDismiss = { showDeveloperDialog = false })
        }
    }
}

// --- Reusable Sub-components ---

@Composable
fun SyncOptionRow(title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = NimbusAccentBlue,
                unselectedColor = NimbusTextHint
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) NimbusTextWhite else NimbusTextHint
        )
    }
}

@Composable
fun SettingsInfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = NimbusTextWhite
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = NimbusTextHint
        )
    }
}

@Composable
fun SettingsActionRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = NimbusTextWhite
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = NimbusTextHint,
            modifier = Modifier.size(20.dp)
        )
    }
}