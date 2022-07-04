package org.qnga.trekarta.maps.catalog

import org.qnga.trekarta.maps.util.WmtsMap
import org.qnga.trekarta.maps.util.WmtsTile
import java.net.URI

internal class BritainOsRoad(val accessToken: String) : TilesProvider {

    private val baseUrl: String =
        "https://api.os.uk/maps/raster/v1/wmts"

    private val wmtsParams = WmtsMap(
        matrixSet = "EPSG:3857",
        layer = "Road_3857",
        format =  "image/png",
        style = "default"
    ).toQueryParameters()

    override val identifier: String =
        "uk.os.road"

    override val title: String =
        "Ordnance Survey UK Road Map"

    override fun tileUrl(zoom: Int, x: Int, y: Int): String {
        val tileParams =
            WmtsTile(
                zoom = zoom,
                tileX = x,
                tileY = y
            ).toQueryParameters()

        val tokenParam =
            accessToken to accessToken

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