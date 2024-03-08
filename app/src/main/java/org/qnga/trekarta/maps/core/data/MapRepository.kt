package org.qnga.trekarta.maps.core.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.qnga.trekarta.maps.core.handlers.Handler
import org.qnga.trekarta.maps.core.handlers.WmtsKvpHandler
import org.qnga.trekarta.maps.core.maps.BritainOsOutdoorProvider
import org.qnga.trekarta.maps.core.maps.BritainOsRoadProvider
import org.qnga.trekarta.maps.core.maps.CustomWmtsKvpSettings
import org.qnga.trekarta.maps.core.maps.FranceIgnScan25Provider
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.core.maps.SpainIgnMtnProvider

data class Map(
    val id: String,
    val title: String,
    val provider: String,
    val settings: MapSettings
) {

    fun createHandler(): Handler =
        when (settings) {
            is FranceIgnScan25Provider.Settings ->
                FranceIgnScan25Provider.createHandler(settings)
            SpainIgnMtnProvider.Settings ->
                SpainIgnMtnProvider.createHandler()
            is BritainOsRoadProvider.Settings ->
                BritainOsRoadProvider.createHandler(settings)
            is BritainOsOutdoorProvider.Settings ->
                BritainOsOutdoorProvider.createHandler(settings)
            is CustomWmtsKvpSettings ->
                WmtsKvpHandler(
                    id = settings.id,
                    title = settings.title,
                    minZoom = settings.minZoom,
                    maxZoom = settings.maxZoom,
                    serviceUrl = settings.serviceUrl,
                    matrixSet = settings.matrixSet,
                    layer = settings.layer,
                    format = settings.format,
                    style = settings.style,
                    otherParams = settings.otherParams
                )
        }

    companion object {

        fun fromSettings(settings: MapSettings): Map =
            when (settings) {
                SpainIgnMtnProvider.Settings ->
                    Map(
                        id = SpainIgnMtnProvider.id,
                        title = SpainIgnMtnProvider.title,
                        provider = SpainIgnMtnProvider.provider,
                        settings = settings
                    )
                is FranceIgnScan25Provider.Settings ->
                    Map(
                        id = FranceIgnScan25Provider.id,
                        title = FranceIgnScan25Provider.title,
                        provider = FranceIgnScan25Provider.provider,
                        settings = settings
                    )
                is BritainOsRoadProvider.Settings ->
                    Map(
                        id = BritainOsRoadProvider.id,
                        title = BritainOsRoadProvider.title,
                        provider = BritainOsRoadProvider.provider,
                        settings = settings
                    )
                is BritainOsOutdoorProvider.Settings ->
                    Map(
                        id = BritainOsOutdoorProvider.id,
                        title = BritainOsOutdoorProvider.title,
                        provider = BritainOsOutdoorProvider.provider,
                        settings = settings
                    )
                is CustomWmtsKvpSettings ->
                    Map(
                        id = settings.id,
                        title = settings.title,
                        provider = settings.provider,
                        settings = settings
                    )
            }
    }
}

class MapRepository(
    private val mapDao: MapDao
) {

    private val coroutineScope: CoroutineScope =
        MainScope()

    private val allProviders: List<MapProvider> =
        listOf(
            FranceIgnScan25Provider,
            SpainIgnMtnProvider,
            BritainOsRoadProvider,
            BritainOsOutdoorProvider
        )

    val userMaps: Flow<List<Map>> = mapDao
        .getAll()
        .map { userMaps ->
            userMaps.map { it.toMap() }
        }

    val unusedProviders: Flow<List<MapProvider>> = userMaps
        .map { usedMaps ->
            val usedIdentifiers = usedMaps
                .map { it.id }
                .toSet()
            
           allProviders
               .filter { it.id !in usedIdentifiers  }
        }

    fun upsertMap(settings: MapSettings) {
        coroutineScope.launch {
            val mapEntity = settings.toMapEntity()
            mapDao.upsert(mapEntity)
        }
    }

    fun removeMap(settings: MapSettings) {
        coroutineScope.launch {
            val mapEntity = settings.toMapEntity()
            mapDao.delete(mapEntity)
        }
    }

    private fun MapSettings.toMapEntity(): MapEntity {
        val id = Map.fromSettings(this).id
        val settingsAsString = Json.encodeToString(this)
        return MapEntity(id, settingsAsString)
    }

    private fun MapEntity.toMap(): Map {
        val settings: MapSettings =  Json.decodeFromString(settings)
        return Map.fromSettings(settings)
    }
}
