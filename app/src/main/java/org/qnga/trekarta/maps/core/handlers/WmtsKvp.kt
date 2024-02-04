package org.qnga.trekarta.maps.core.handlers

import java.net.URI

class WmtsKvpHandler(
    id: String,
    title: String,
    minZoom: Int,
    maxZoom: Int,
    val serviceUrl: String,
    val matrixSet: String,
    val layer: String,
    val format: String,
    val style: String,
    val otherParams: List<Pair<String, String>> = emptyList(),
) : AbstractHandler(id, title, minZoom, maxZoom) {

    override fun tile(zoom: Int, x: Int, y: Int): String {
        val wmtsParams = WmtsKvpMap(
            matrixSet = matrixSet,
            layer =  layer,
            format = format,
            style = style
        ).toQueryParameters()

        val tileParams = WmtsKvpTile(
            zoom = zoom,
            tileX = x,
            tileY = y
        ).toQueryParameters()

        val query =
            (wmtsParams + tileParams + otherParams)
                .joinToString("&") { "${it.first}=${it.second}" }

        val baseUri = URI(serviceUrl)
        val tileUri = URI(baseUri.scheme, baseUri.authority, baseUri.path, query, null)
        return tileUri.toASCIIString()
    }
}

data class WmtsKvpMap(
    val matrixSet: String,

    val layer: String,

    val format: String,

    val style: String,
) {
    fun toQueryParameters(): List<Pair<String, String>> =
        listOf(
            "service" to "WMTS",
            "version" to "1.0.0",
            "request" to "GetTile",
            "layer" to layer,
            "style" to style,
            "tilematrixset" to matrixSet,
            "format" to format
        )
}

data class WmtsKvpTile(
    val zoom: Int,
    val tileX: Int,
    val tileY: Int
) {
    fun toQueryParameters(): List<Pair<String, String>> =
        listOf(
            "tilematrix" to zoom.toString(),
            "tilerow" to tileY.toString(),
            "tilecol" to tileX.toString(),
        )
}
