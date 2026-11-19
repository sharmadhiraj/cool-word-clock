package com.sharmadhiraj.coolwordclock.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {

    private val store = context.applicationContext.dataStore

    val settings: Flow<Settings> = store.data.map { prefs ->
        val defaults = Settings()
        Settings(
            chime = prefs[CHIME] ?: defaults.chime,
            minuteDots = prefs[MINUTE_DOTS] ?: defaults.minuteDots,
            nightDim = prefs[NIGHT_DIM] ?: defaults.nightDim,
            accent = Accent.entries.firstOrNull { it.name == prefs[ACCENT] } ?: defaults.accent,
        )
    }

    suspend fun update(settings: Settings) {
        store.edit { prefs ->
            prefs[CHIME] = settings.chime
            prefs[MINUTE_DOTS] = settings.minuteDots
            prefs[NIGHT_DIM] = settings.nightDim
            prefs[ACCENT] = settings.accent.name
        }
    }

    private companion object {
        val CHIME = booleanPreferencesKey("chime")
        val MINUTE_DOTS = booleanPreferencesKey("minute_dots")
        val NIGHT_DIM = booleanPreferencesKey("night_dim")
        val ACCENT = stringPreferencesKey("accent")
    }
}
