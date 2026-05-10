package com.example.iotapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapp.data.datastore.SettingsDataStore
import com.example.iotapp.data.model.ConnectionSettings
import com.example.iotapp.data.model.LampState
import com.example.iotapp.data.repository.LampRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class LampUiState(
    val settings: ConnectionSettings = ConnectionSettings(),
    val lamps: List<LampState> = emptyList(),
    val isLoading: Boolean = false,
    val message: String = "Vul je IP-adres en username in via Menu > Instellingen.",
    val simulationMode: Boolean = true
)

class LampViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LampRepository()
    private val settingsDataStore = SettingsDataStore(application.applicationContext)

    var uiState = mutableStateOf(
        LampUiState(
            lamps = demoLamps()
        )
    )
        private set

    init {
        viewModelScope.launch {
            settingsDataStore.settings.collectLatest { settings ->
                uiState.value = uiState.value.copy(settings = settings)
                loadLights()
            }
        }
    }

    fun loadLights() {
        val settings = uiState.value.settings
        if (settings.ipAddress.isBlank() || settings.username.isBlank()) {
            uiState.value = uiState.value.copy(
                lamps = demoLamps(),
                simulationMode = true,
                isLoading = false,
                message = "Simulatie actief: vul IP-adres en username in via Menu > Instellingen."
            )
            return
        }

        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true, message = "Verbinding maken met Hue API...")
            try {
                val lights = repository.getLights(settings)
                uiState.value = uiState.value.copy(
                    lamps = lights,
                    isLoading = false,
                    simulationMode = false,
                    message = "Verbonden met http://${settings.ipAddress}/api/${settings.username}/lights"
                )
            } catch (exception: Exception) {
                uiState.value = uiState.value.copy(
                    lamps = demoLamps(),
                    isLoading = false,
                    simulationMode = true,
                    message = "Geen verbinding met Hue API. De app toont nu simulatiegegevens. Controleer IP-adres, username en emulator."
                )
            }
        }
    }

    fun saveSettings(ipAddress: String, username: String) {
        viewModelScope.launch {
            settingsDataStore.saveSettings(ipAddress, username)
        }
    }

    fun resetConnection() {
        viewModelScope.launch {
            settingsDataStore.resetSettings()
            uiState.value = uiState.value.copy(
                lamps = demoLamps(),
                simulationMode = true,
                message = "Connectie gereset. Simulatie actief."
            )
        }
    }

    fun toggleLamp(lampId: String) {
        val currentLamp = uiState.value.lamps.firstOrNull { it.id == lampId } ?: return
        val newState = !currentLamp.on

        if (uiState.value.simulationMode) {
            updateDemoLamp(lampId) { it.copy(on = newState) }
            return
        }

        viewModelScope.launch {
            try {
                repository.setPower(uiState.value.settings, lampId, newState)
                loadLights()
            } catch (_: Exception) {
                updateDemoLamp(lampId) { it.copy(on = newState) }
                uiState.value = uiState.value.copy(
                    simulationMode = true,
                    message = "API-oproep mislukt. Wijziging werd gesimuleerd."
                )
            }
        }
    }

    fun setBrightness(lampId: String, brightness: Int) {
        if (uiState.value.simulationMode) {
            updateDemoLamp(lampId) { it.copy(brightness = brightness.coerceIn(1, 254)) }
            return
        }

        viewModelScope.launch {
            try {
                repository.setBrightness(uiState.value.settings, lampId, brightness)
                loadLights()
            } catch (_: Exception) {
                updateDemoLamp(lampId) { it.copy(brightness = brightness.coerceIn(1, 254)) }
                uiState.value = uiState.value.copy(
                    simulationMode = true,
                    message = "Helderheid kon niet naar de API gestuurd worden. Wijziging werd gesimuleerd."
                )
            }
        }
    }

    fun setColor(lampId: String, colorName: String) {
        val rgb = when (colorName) {
            "Rood" -> Triple(255, 0, 0)
            "Groen" -> Triple(0, 255, 0)
            "Blauw" -> Triple(0, 0, 255)
            else -> Triple(255, 255, 255)
        }

        if (uiState.value.simulationMode) {
            updateDemoLamp(lampId) { it.copy(on = true, colorName = colorName) }
            return
        }

        viewModelScope.launch {
            try {
                repository.setColor(uiState.value.settings, lampId, rgb.first, rgb.second, rgb.third)
                loadLights()
            } catch (_: Exception) {
                updateDemoLamp(lampId) { it.copy(on = true, colorName = colorName) }
                uiState.value = uiState.value.copy(
                    simulationMode = true,
                    message = "Kleur kon niet naar de API gestuurd worden. Wijziging werd gesimuleerd."
                )
            }
        }
    }

    fun getLamp(lampId: String): LampState? {
        return uiState.value.lamps.firstOrNull { it.id == lampId }
    }

    private fun updateDemoLamp(lampId: String, update: (LampState) -> LampState) {
        val updated = uiState.value.lamps.map { lamp ->
            if (lamp.id == lampId) update(lamp) else lamp
        }
        uiState.value = uiState.value.copy(lamps = updated, message = "Simulatie bijgewerkt.")
    }

    private fun demoLamps(): List<LampState> = listOf(
        LampState(id = "1", name = "Lamp 1", on = true, brightness = 180, colorName = "Rood"),
        LampState(id = "2", name = "Lamp 2", on = false, brightness = 100, colorName = "Wit"),
        LampState(id = "3", name = "Lamp 3", on = true, brightness = 220, colorName = "Blauw")
    )
}
