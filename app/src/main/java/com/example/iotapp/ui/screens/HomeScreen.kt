package com.example.iotapp.ui.screens

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iotapp.data.model.LampState
import com.example.iotapp.viewmodel.LampViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    viewModel: LampViewModel,
    onOpenLamp: (String) -> Unit
) {
    val state = viewModel.uiState.value
    var showSettings by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("IoT Hue Lampen") },
                actions = {
                    TextButton(onClick = { showMenu = true }) { Text("Menu") }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Instellingen") },
                            onClick = {
                                showMenu = false
                                showSettings = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Reset connectie") },
                            onClick = {
                                showMenu = false
                                viewModel.resetConnection()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Vernieuwen") },
                            onClick = {
                                showMenu = false
                                viewModel.loadLights()
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = if (state.simulationMode) "Modus: simulatie" else "Modus: REST API",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.message, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.lamps) { lamp ->
                    LampOverviewCard(
                        lamp = lamp,
                        onToggle = { viewModel.toggleLamp(lamp.id) },
                        onOpen = { onOpenLamp(lamp.id) }
                    )
                }
            }
        }
    }

    if (showSettings) {
        SettingsDialog(
            currentIp = state.settings.ipAddress,
            currentUsername = state.settings.username,
            onDismiss = { showSettings = false },
            onSave = { ip, username ->
                viewModel.saveSettings(ip, username)
                showSettings = false
            }
        )
    }
}

@Composable
fun LampOverviewCard(
    lamp: LampState,
    onToggle: () -> Unit,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = lamp.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Status: ${if (lamp.on) "AAN" else "UIT"}")
            Text(text = "Helderheid: ${lamp.brightness}/254")
            Text(text = "Kleur: ${lamp.colorName}")
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onToggle) {
                    Text(if (lamp.on) "Zet uit" else "Zet aan")
                }
                Button(onClick = onOpen) {
                    Text("Instellen")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LampDetailScreen(
    viewModel: LampViewModel,
    lampId: String,
    onBack: () -> Unit
) {
    val state = viewModel.uiState.value
    val lamp = viewModel.getLamp(lampId) ?: state.lamps.firstOrNull()
    var showSettings by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var sliderValue by remember(lamp?.brightness) { mutableStateOf((lamp?.brightness ?: 100).toFloat()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(lamp?.name ?: "Lamp instellen") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Terug") }
                },
                actions = {
                    TextButton(onClick = { showMenu = true }) { Text("Menu") }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Instellingen") },
                            onClick = {
                                showMenu = false
                                showSettings = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Reset connectie") },
                            onClick = {
                                showMenu = false
                                viewModel.resetConnection()
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (lamp == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Geen lamp gevonden")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (lamp.on) "${lamp.name} is AAN" else "${lamp.name} is UIT",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(text = "Kleur: ${lamp.colorName}")
                Text(text = state.message)

                Button(onClick = { viewModel.toggleLamp(lamp.id) }) {
                    Text(if (lamp.on) "Lamp uitzetten" else "Lamp aanzetten")
                }

                Text("Helderheid: ${sliderValue.toInt()}/254")
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 1f..254f,
                    onValueChangeFinished = {
                        viewModel.setBrightness(lamp.id, sliderValue.toInt())
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.setColor(lamp.id, "Rood") }) { Text("Rood") }
                    Button(onClick = { viewModel.setColor(lamp.id, "Groen") }) { Text("Groen") }
                    Button(onClick = { viewModel.setColor(lamp.id, "Blauw") }) { Text("Blauw") }
                }
                Button(onClick = { viewModel.setColor(lamp.id, "Wit") }) { Text("Wit") }
            }
        }
    }

    if (showSettings) {
        SettingsDialog(
            currentIp = state.settings.ipAddress,
            currentUsername = state.settings.username,
            onDismiss = { showSettings = false },
            onSave = { ip, username ->
                viewModel.saveSettings(ip, username)
                showSettings = false
            }
        )
    }
}

@Composable
fun SettingsDialog(
    currentIp: String,
    currentUsername: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var ipAddress by remember { mutableStateOf(currentIp) }
    var username by remember { mutableStateOf(currentUsername) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Connectie instellen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Voor Philips Hue of Hue Emulator gebruik je het IP-adres van de bridge/emulator en de username/secret.")
                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text("IP-adres, bv. 192.168.1.50:8000") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username / secret") },
                    singleLine = true
                )
                Text("REST URLs in deze app: GET /api/username/lights en PUT /api/username/lights/1/state")
            }
        },
        confirmButton = {
            Button(onClick = { onSave(ipAddress, username) }) { Text("Opslaan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuleren") }
        }
    )
}
