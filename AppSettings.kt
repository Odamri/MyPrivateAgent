package com.myprivateagent.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class AppSettings(private val context: Context) {
    private val BASE_URL = stringPreferencesKey("base_url")
    private val PIN = stringPreferencesKey("pin")
    private val PIN_ENABLED = booleanPreferencesKey("pin_enabled")

    val baseUrlFlow: Flow<String> = context.dataStore.data.map { it[BASE_URL] ?: "" }
    val pinFlow: Flow<String> = context.dataStore.data.map { it[PIN] ?: "" }
    val pinEnabledFlow: Flow<Boolean> = context.dataStore.data.map { it[PIN_ENABLED] ?: false }

    suspend fun setBaseUrl(url: String) {
        context.dataStore.edit { it[BASE_URL] = url.trim().removeSuffix("/") }
    }

    suspend fun setPin(pin: String) {
        context.dataStore.edit { it[PIN] = pin }
    }

    suspend fun setPinEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PIN_ENABLED] = enabled }
    }
}
