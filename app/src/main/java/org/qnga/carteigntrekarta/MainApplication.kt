package org.qnga.carteigntrekarta

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainApplication : Application() {

    private val coroutineScope: CoroutineScope =
        MainScope()
    private val Context.dataStore: DataStore<Preferences> by
        preferencesDataStore(name = "settings")
    private val serviceKey: Preferences.Key<String> =
        stringPreferencesKey("IGN_SERVICE_KEY")
    private val keyMutable: MutableStateFlow<String?> =
        MutableStateFlow(null)

    val key : StateFlow<String?> = keyMutable

    override fun onCreate() {
        super.onCreate()

        dataStore.data
            .map { it[serviceKey]?.takeUnless(String::isEmpty) }
            .onEach { keyMutable.value = it }
            .launchIn(coroutineScope)
    }

    fun updateKey(key: String?) {
        coroutineScope.launch { updateKeyAsync(key) }
    }

    private suspend fun updateKeyAsync(key: String?) {
        dataStore.edit { settings -> settings[serviceKey] = key.orEmpty() }
    }
}