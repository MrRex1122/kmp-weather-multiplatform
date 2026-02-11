package com.example.kotlinpet.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kotlinpet.model.DailyForecast
import com.example.kotlinpet.model.Location
import com.example.kotlinpet.model.WeatherSnapshot
import com.example.kotlinpet.util.WeatherDescriptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherApp() {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val descriptions = remember { WeatherDescriptions() }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Weather") },
                    actions = {
                        IconButton(onClick = { viewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                LocationSelector(
                    locations = viewModel.locations,
                    selected = viewModel.selectedLocation,
                    onSelect = viewModel::selectLocation
                )
                Spacer(modifier = Modifier.height(16.dp))
                when (val uiState = state) {
                    WeatherUiState.Loading -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is WeatherUiState.Error -> {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Try again")
                        }
                    }
                    is WeatherUiState.Loaded -> {
                        WeatherContent(
                            snapshot = uiState.snapshot,
                            descriptions = descriptions
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationSelector(
    locations: List<Location>,
    selected: Location,
    onSelect: (Location) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selected.name,
            onValueChange = {},
            label = { Text("Location") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            locations.forEach { location ->
                DropdownMenuItem(
                    text = { Text(location.name) },
                    onClick = {
                        expanded = false
                        onSelect(location)
                    }
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(
    snapshot: WeatherSnapshot,
    descriptions: WeatherDescriptions
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = snapshot.location.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${snapshot.current.temperatureC.toInt()}°C",
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = descriptions.forCode(snapshot.current.weatherCode),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Wind ${snapshot.current.windSpeedKmh.toInt()} km/h",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "7-day forecast",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(snapshot.daily) { day ->
                ForecastRow(day = day, descriptions = descriptions)
            }
        }
    }
}

@Composable
private fun ForecastRow(
    day: DailyForecast,
    descriptions: WeatherDescriptions
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = day.dateIso, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = descriptions.forCode(day.weatherCode),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "${day.minTempC.toInt()}° / ${day.maxTempC.toInt()}°",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
