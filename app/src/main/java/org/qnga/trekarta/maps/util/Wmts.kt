package org.qnga.trekarta.maps.util

data class WmtsMap(
    val matrixSet: String,

    val layer: String,

    val format: String,

    val style: String,
) {
    fun toQueryParameters(): Map<String, String> =
        mapOf(
            "service" to "WMTS",
            "version" to "1.0.0",
            "request" to "GetTile",
            "layer" to layer,
            "style" to style,
            "tilematrixset" to matrixSet,
            "format" to format
        )

}

data class WmtsTile(
    val zoom: Int,
    val tileX: Int,
    val tileY: Int
) {
    fun toQueryParameters(): Map<String, String> =
        mapOf(
            "tilematrix" to zoom.toString(),
            "tilerow" to tileY.toString(),
            "tilecol" to tileX.toString(),
        )
}