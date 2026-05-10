package com.example.iotapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.iotapp.data.model.ConnectionSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "iot_settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        private val IP_ADDRESS = stringPreferencesKey("ip_address")
        private val USERNAME = stringPreferencesKey("username")
    }

    val settings: Flow<ConnectionSettings> = context.dataStore.data.map { preferences ->
        ConnectionSettings(
            ipAddress = preferences[IP_ADDRESS] ?: "",
            username = preferences[USERNAME] ?: ""
        )
    }

    suspend fun saveSettings(ipAddress: String, username: String) {
        context.dataStore.edit { preferences ->
            preferences[IP_ADDRESS] = ipAddress.trim()
            preferences[USERNAME] = username.trim()
        }
    }

    suspend fun resetSettings() {
        context.dataStore.edit { it.clear() }
    }
}
