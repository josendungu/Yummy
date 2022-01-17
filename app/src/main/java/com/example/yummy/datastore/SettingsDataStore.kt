package com.example.yummy.datastore

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.yummy.YummyApplication
import com.example.yummy.common.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(app: YummyApplication){


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val context = app.baseContext
    private val scope = CoroutineScope(Main)
    val isDark = mutableStateOf(false)


    init {
        observeDataStore()
    }

    private fun observeDataStore() {

        context.dataStore.data.onEach { preferences ->
            preferences[DARK_KEY_THEME]?.let { isDarkTheme : Boolean ->
                isDark.value = isDarkTheme
            }
        }.launchIn(scope)

    }

    fun toggleTheme(){
        scope.launch {
            context.dataStore.edit {
                val current = it[DARK_KEY_THEME]?: false
                Log.d(TAG, "toggleTheme: ${!current}")
                it[DARK_KEY_THEME] = !current
            }
        }
    }

    companion object {
        private val DARK_KEY_THEME = booleanPreferencesKey("dark_theme_key")
    }


}