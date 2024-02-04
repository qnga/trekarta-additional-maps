package org.qnga.trekarta.maps.core.maps

import kotlinx.serialization.Serializable
import org.qnga.trekarta.maps.core.handlers.Handler
import org.qnga.trekarta.maps.core.handlers.WmtsKvpHandler

object SpainIgnMtnProvider : MapProvider {

    override val id: String =
        "es.ign.wmts.mtn"

    override val title: String =
        "Cartografía de España del IGN"

    override val provider: String =
        "Instituto Geográfico Nacional"


    @Serializable
    data object Settings : MapSettings

    fun createHandler(): Handler =
        WmtsKvpHandler(
            id = id,
            title =  title,
            serviceUrl = "https://www.ign.es/wmts/mapa-raster",
            matrixSet = "GoogleMapsCompatible",
            layer =  "MTN",
            format = "image/jpeg",
            style = "default",
            minZoom = 1,
            maxZoom = 20
        )
}
