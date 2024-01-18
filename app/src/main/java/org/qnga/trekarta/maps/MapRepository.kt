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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.qnga.trekarta.maps.catalog.MapProvider
import org.qnga.trekarta.maps.catalog.MapProviders
import org.qnga.trekarta.maps.catalog.OpenAccessMapProvider
import org.qnga.trekarta.maps.catalog.TiledMap
import org.qnga.trekarta.maps.catalog.TokenAccessMapProvider

internal class MapRepository(
    private val context: Context
) {

    data class MapHolder(
        val provider: MapProvider,
        val source: TiledMap
    )

    private val coroutineScope: CoroutineScope =
        MainScope()
    
    private val Context.secretDataStore: DataStore<Preferences> by
        preferencesDataStore(name = "secrets")

    val providers: List<MapProvider> =
        MapProviders

    val tokens: Flow<Map<String, String>> =
        context.secretDataStore.data
            .map { computeSecrets(it) }

    private fun computeSecrets(preferences: Preferences): Map<String, String> {
        val secrets = providers.mapNotNull { provider ->
            when (provider) {
                is OpenAccessMapProvider ->
                    null
                is TokenAccessMapProvider -> {
                    preferences[stringPreferencesKey(provider.identifier)]
                        ?.takeUnless(String::isBlank)
                        ?.let {  provider.identifier to it }
                }
            }
        }

        return secrets.toMap()
    }

    val maps: Flow<Map<String, MapHolder>> =
         tokens.map{ computeMaps(it) }

    private fun computeMaps(secrets: Map<String, String>): Map<String, MapHolder> {
        val maps = providers.mapNotNull { provider ->
            when (provider) {
                is OpenAccessMapProvider ->
                    provider.identifier to MapHolder(
                        provider,
                        provider.createTileSource()
                    )
                is TokenAccessMapProvider ->
                    secrets[provider.identifier]
                        ?.let {  provider.identifier to MapHolder(
                            provider,
                            provider.createTileSource(it))
                        }
            }
        }

        return maps.toMap()
    }

    fun setAccessToken(mapIdentifier: String, accessToken: String?) = coroutineScope. launch {
        val key = stringPreferencesKey(mapIdentifier)
        updateSecret(key, accessToken)
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
