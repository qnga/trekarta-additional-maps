package org.qnga.trekarta.maps.catalog

val MapProviders: List<MapProvider> = listOf(
    FranceIgnMapProvider,
    SpainIgnMtnProvider,
    BritainOsRoadProvider,
    BritainOsOutdoorProvider
)

sealed interface MapProvider {

    val identifier: String

    val title: String
}

interface OpenAccessMapProvider : MapProvider {

    fun createTileSource(): TiledMap

}

interface TokenAccessMapProvider : MapProvider {

    fun createTileSource(token: String): TiledMap
}

interface TiledMap {

    val minZoom: Int

    val maxZoom: Int

    fun tileUrl(zoom: Int, x: Int, y: Int): String
}
