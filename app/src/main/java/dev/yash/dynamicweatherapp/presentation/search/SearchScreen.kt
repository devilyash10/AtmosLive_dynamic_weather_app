package dev.yash.dynamicweatherapp.presentation.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AddLocationAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.yash.dynamicweatherapp.domain.model.LocationSearchResult
import dev.yash.dynamicweatherapp.presentation.common.GlassCard
import dev.yash.dynamicweatherapp.presentation.common.getWeatherIconResource
import dev.yash.dynamicweatherapp.presentation.common.toFormattedTemp
import dev.yash.dynamicweatherapp.presentation.navigation.Screen
import dev.yash.dynamicweatherapp.presentation.theme.NimbusAccentBlue
import dev.yash.dynamicweatherapp.presentation.theme.NimbusDark
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextHint
import dev.yash.dynamicweatherapp.presentation.theme.NimbusTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NimbusDark)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // MATCHING MOCKUP: Small, styled header
        Text(
            text = "MY CITIES",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            ),
            color = NimbusAccentBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // MATCHING MOCKUP: Search Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for a city...", color = NimbusTextHint) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NimbusTextHint
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = NimbusTextWhite,
                unfocusedTextColor = NimbusTextWhite,
                cursorColor = NimbusAccentBlue,
                focusedBorderColor = NimbusAccentBlue,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // MATCHING MOCKUP: GPS Button
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        // FIX 1: Flat Navigation for GPS Button
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "GPS",
                        tint = NimbusAccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Use Current GPS Location",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NimbusAccentBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Subtle, premium instructional hint
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = NimbusTextHint.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Long press a saved location to delete it",
                    style = MaterialTheme.typography.labelMedium,
                    color = NimbusTextHint.copy(alpha = 0.7f)
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        when {
            state.isSearching -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    CircularProgressIndicator(color = NimbusAccentBlue)
                }
            }

            state.searchQuery.length >= 2 && state.error != null -> {
                Text(
                    text = state.error!!,
                    color = NimbusTextHint,
                    modifier = Modifier.padding(16.dp)
                )
            }

            state.searchQuery.length >= 2 && state.searchResults.isNotEmpty() -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(state.searchResults.size) { index ->
                        SearchResultItem(
                            location = state.searchResults[index],
                            onClick = {
                                viewModel.saveLocation(state.searchResults[index])
                                // FIX 2: Flat Navigation for Active Search Click
                                navController.navigate(
                                    Screen.Home.passCoordinates(
                                        state.searchResults[index].latitude,
                                        state.searchResults[index].longitude,
                                        state.searchResults[index].name
                                    )
                                ) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }

            else -> {
                if (state.savedLocations.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        itemsIndexed(state.savedLocations) { index, dbLocationState ->
                            SavedLocationRow(
                                state = dbLocationState,
                                isFirstItem = index == 0,
                                temperatureUnit = state.temperatureUnit,
                                onDeleteClick = { viewModel.deleteLocation(dbLocationState.location) },
                                onClick = {
                                    // FIX 3: Flat Navigation for Saved Location Click
                                    navController.navigate(
                                        Screen.Home.passCoordinates(
                                            dbLocationState.location.latitude,
                                            dbLocationState.location.longitude,
                                            dbLocationState.location.name
                                        )
                                    ) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                } else {
                    // The beautiful empty state when no cities are saved!
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 100.dp)
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.extraLarge,
                                color = NimbusAccentBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(100.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AddLocationAlt,
                                    contentDescription = "No Cities",
                                    tint = NimbusAccentBlue,
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "No Cities Saved",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = NimbusTextWhite
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Use the search bar above to find a city and add it to your tracking list.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NimbusTextHint,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


// Active Network Search Results
@Composable
fun SearchResultItem(location: LocationSearchResult, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = NimbusAccentBlue, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = location.name, style = MaterialTheme.typography.titleMedium, color = NimbusTextWhite)
                val subtitle = if (!location.admin1.isNullOrEmpty()) "${location.admin1}, ${location.country}" else location.country
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = NimbusTextHint)
            }
        }
    }
}

// Saved Locations with Live Weather Data
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedLocationRow(
    state: SavedLocationWeatherState,
    isFirstItem: Boolean,
    temperatureUnit: dev.yash.dynamicweatherapp.domain.settings.TemperatureUnit,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            //Long Press to delete!
            .combinedClickable(
                onClick = onClick,
                onLongClick = onDeleteClick
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LEFT SIDE: Blue Dot, City Name, Country Pill, Condition Text
            Row(verticalAlignment = Alignment.CenterVertically) {
                // The blue indicator dot
                if (isFirstItem) {
                    Box(modifier = Modifier.size(8.dp).background(NimbusAccentBlue, CircleShape))
                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    Spacer(modifier = Modifier.width(20.dp))
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.location.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = NimbusTextWhite
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Country Pill (e.g., "US", "GB")
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.location.country.take(2).uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = NimbusTextHint
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Condition Text (e.g., "Partly Cloudy")
                    Text(
                        text = state.weatherInfo?.current?.condition ?: "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NimbusTextHint
                    )
                }
            }

            // RIGHT SIDE: Weather Icon and Temperature
            if (state.isLoading) {
                CircularProgressIndicator(color = NimbusAccentBlue, modifier = Modifier.size(24.dp))
            } else if (state.isError || state.weatherInfo == null) {
                // Shows a cloud-off icon if the network failed
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.CloudOff,
                    contentDescription = "Offline",
                    tint = NimbusTextHint,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = getWeatherIconResource(state.weatherInfo.current.iconId)),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = state.weatherInfo.current.temperature.toFormattedTemp(temperatureUnit),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = NimbusTextWhite
                    )
                }
            }
        }
    }
}