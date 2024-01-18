package org.qnga.trekarta.maps.catalog

import java.net.URI

internal object BritainOsRoadProvider : TokenAccessMapProvider {

    override val identifier: String =
        "uk.os.road"

    override val title: String =
        "Ordnance Survey UK Road Map"

    override fun createTileSource(token: String): TiledMap =
        BritainOsRoadMap(token)
}

private class BritainOsRoadMap(val accessToken: String) : TiledMap {

    private val baseUrl: String =
        "https://api.os.uk/maps/raster/v1/wmts"

    private val wmtsParams = WmtsMap(
        matrixSet = "EPSG:3857",
        layer = "Road_3857",
        format =  "image/png",
        style = "default"
    ).toQueryParameters()

    override val minZoom: Int = 7

    override val maxZoom: Int = 20


    override fun tileUrl(zoom: Int, x: Int, y: Int): String {
        val tileParams =
            WmtsTile(
                zoom = zoom,
                tileX = x,
                tileY = y
            ).toQueryParameters()

        val tokenParam =
            "key" to accessToken

        val query =
            (wmtsParams + tileParams + tokenParam)
            .map { "${it.key}=${it.value}" }
            .joinToString("&")

        val baseUri =
            URI(baseUrl)

        val tileUri =
            URI(baseUri.scheme, baseUri.authority, baseUri.path, query, null)

        return tileUri.toASCIIString()
    }
}
