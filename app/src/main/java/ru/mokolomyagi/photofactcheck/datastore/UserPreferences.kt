package ru.mokolomyagi.photofactcheck.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Расширение на Context для доступа к DataStore
val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

object PreferenceKeys {
    val COORD_FORMAT = stringPreferencesKey("coord_format")
    val THEME_KEY = stringPreferencesKey("theme_mode")
}

class UserPreferencesRepository(private val context: Context) {

    val coordFormatFlow: Flow<String> = context.userPreferencesDataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.COORD_FORMAT] ?: "decimal"
        }

    suspend fun setCoordFormat(value: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[PreferenceKeys.COORD_FORMAT] = value
        }
    }

    val themeFlow: Flow<ThemeMode> = context.userPreferencesDataStore.data
        .map { preferences ->
            ThemeMode.fromValue(preferences[PreferenceKeys.THEME_KEY] ?: ThemeMode.SYSTEM.value)
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_KEY] = mode.value
        }
    }
}
