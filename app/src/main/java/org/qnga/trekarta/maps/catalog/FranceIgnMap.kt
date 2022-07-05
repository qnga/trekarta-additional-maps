package org.qnga.trekarta.maps.catalog

import java.net.URI

object FranceIgnMapProvider : TokenAccessMapProvider {

    override val identifier: String =
        "fr.ign.maps"

    override val title: String =
        "Carte IGN de la France"

    override fun createTileSource(token: String): TiledMap =
        FranceIgnMap(token)
}

class FranceIgnMap(accessToken: String) : TiledMap {

    private val baseUrl: String =
        "https://wxs.ign.fr/$accessToken/geoportail/wmts"

    private val wmtsParams = WmtsMap(
        matrixSet = "PM",
        layer = "GEOGRAPHICALGRIDSYSTEMS.MAPS",
        format = "image/jpeg",
        style = "normal"
    ).toQueryParameters()

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
