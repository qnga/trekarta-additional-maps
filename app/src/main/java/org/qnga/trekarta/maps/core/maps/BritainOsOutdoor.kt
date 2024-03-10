package org.qnga.trekarta.maps.core.maps

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.qnga.trekarta.maps.core.handlers.Handler
import org.qnga.trekarta.maps.core.handlers.WmtsKvpHandler

object BritainOsOutdoorProvider : MapProvider {

    override val id: String =
        "uk.os.api.wmts.outdoor"

    override val title: String =
        "UK Outdoor Map"

    override val provider: String =
        "Ordnance Survey"

    @Serializable
    @SerialName("BritainOsOutdoorSettings")
    data class Settings(
        val accessToken: String
    ) : MapSettings

    fun createHandler(settings: Settings): Handler =
        WmtsKvpHandler(
            id = id,
            title = title,
            minZoom = 7,
            maxZoom = 20,
            serviceUrl =  "https://api.os.uk/maps/raster/v1/wmts",
            matrixSet ="EPSG:3857",
            layer =  "Outdoor_3857",
            format = "image/png",
            style = "default",
            otherParams = listOf("key" to settings.accessToken)
        )
}
