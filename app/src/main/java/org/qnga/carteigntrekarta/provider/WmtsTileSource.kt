package org.qnga.carteigntrekarta.provider

import java.net.URI

internal class WmtsTileSource(private val baseUrl: String) {

    data class TileDescription(
        val layer: String,
        val matrixSet: String,
        val matrix: String,
        val row: Int,
        val column: Int,
        val format: String,
        val style: String
    )

    fun urlForTile(tile: TileDescription): String {
        val params = mutableMapOf(
            "service" to "WMTS",
            "version" to "1.0.0",
            "request" to "GetTile",
            "layer" to tile.layer,
            "style" to tile.style,
            "tilematrixset" to tile.matrixSet,
            "tilematrix" to tile.matrix,
            "tilerow" to tile.row,
            "tilecol" to tile.column,
            "format" to tile.format
        )
        val query = params
            .map { "${it.key}=${it.value}" }
            .joinToString("&")

        val baseUri = URI(baseUrl)
        val tileUri = URI(baseUri.scheme, baseUri.authority, baseUri.path, query, null)
        return tileUri.toASCIIString()
    }
}
