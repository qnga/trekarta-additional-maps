package org.qnga.trekarta.maps

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.qnga.trekarta.maps.catalog.BritainOsRoad
import org.qnga.trekarta.maps.catalog.FranceIgnMap
import org.qnga.trekarta.maps.catalog.SpainIgnMtn
import org.qnga.trekarta.maps.catalog.TilesProvider

class ProviderRepository(
    private val context: Context
) {
    
    private val coroutineScope: CoroutineScope =
        MainScope()

    private val Context.secretDataStore: DataStore<Preferences> by
    preferencesDataStore(name = "secrets")

    private val franceIgnMapKey: Preferences.Key<String> =
        stringPreferencesKey(FranceIgnMap::class.java.name)

    private val britainOsRoadKey: Preferences.Key<String> =
        stringPreferencesKey(BritainOsRoad::class.java.name)

    val providers: Flow<Map<String, TilesProvider>> =
         context.secretDataStore.data
             .mapNotNull { computeProviders(it) }

    private fun computeProviders(preferences: Preferences): Map<String, TilesProvider> {
        val franceIgnMap =
            preferences[franceIgnMapKey]
                ?.takeUnless(String::isBlank)
                ?.let { FranceIgnMap(it) }

        val britainOsRoad =
            preferences[britainOsRoadKey]
                ?.takeUnless(String::isBlank)
                ?.let { BritainOsRoad(it) }

        return listOfNotNull(
            SpainIgnMtn,
            franceIgnMap,
            britainOsRoad
        ).associateBy(TilesProvider::identifier)
    }

    fun setFranceIgnMapToken(token: String?) = coroutineScope.launch {
        updateSecret(franceIgnMapKey, token)
    }

    suspend fun setBritainOsRoadToken(token: String?) = coroutineScope.launch {
        updateSecret(britainOsRoadKey, token)
    }

    private suspend fun<T> updateSecret(identifier: Preferences.Key<T>, secret: T?) {
        context.secretDataStore.edit { preferences ->
            if (secret == null) {
                preferences.remove(identifier)
            } else {
                preferences[identifier] = secret
            }
        }
    }
}
