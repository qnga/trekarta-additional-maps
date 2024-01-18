package org.qnga.trekarta.maps.catalog

import java.net.URI

object SpainIgnMtnProvider : OpenAccessMapProvider {

    override val identifier: String =
        "es.ign.mtn"

    override val title: String =
        "Cartografía de España del IGN"

    override fun createTileSource(): TiledMap =
        SpainIgnMtn()
}

private class SpainIgnMtn : TiledMap {

    private val baseUrl: String =
        "https://www.ign.es/wmts/mapa-raster"

    private val wmtsParams = WmtsMap(
        matrixSet = "GoogleMapsCompatible",
        layer =  "MTN",
        format = "image/jpeg",
        style = "default"
    ).toQueryParameters()

    override val minZoom: Int = 1

    override val maxZoom: Int = 20

    override fun tileUrl(zoom: Int, x: Int, y: Int): String {
        val tileParams = WmtsTile(
            zoom = zoom,
            tileX = x,
            tileY = y
        ).toQueryParameters()

        val query = (wmtsParams + tileParams)
            .map { "${it.key}=${it.value}" }
            .joinToString("&")

        val baseUri = URI(baseUrl)
        val tileUri = URI(baseUri.scheme, baseUri.authority, baseUri.path, query, null)
        return tileUri.toASCIIString()
    }
}
