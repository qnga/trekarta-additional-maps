package org.qnga.trekarta.maps.core.maps

import kotlinx.serialization.Serializable
import org.qnga.trekarta.maps.core.handlers.Handler
import org.qnga.trekarta.maps.core.handlers.WmtsKvpHandler

object FranceIgnScan25Provider : MapProvider {

    override val id: String =
        "fr.geopf.data.wmts.scan25"

    override val title: String =
        "Carte IGN de la France"

    override val provider: String =
        "Institut géographique national"

    @Serializable
    data class Settings(
        val accessToken: String
    ) : MapSettings

    fun createHandler(settings: Settings): Handler =
        WmtsKvpHandler(
            id = id,
            title = title,
            serviceUrl =  "https://data.geopf.fr/private/wmts",
            matrixSet ="PM",
            layer =  "GEOGRAPHICALGRIDSYSTEMS.MAPS",
            format = "image/jpeg",
            style = "normal",
            otherParams = listOf("apikey" to settings.accessToken),
            minZoom = 0,
            maxZoom = 21
        )
}
