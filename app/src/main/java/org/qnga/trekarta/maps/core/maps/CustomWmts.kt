package org.qnga.trekarta.maps.core.maps

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CustomWmtsKvpSettings(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val provider: String,
    val minZoom: Int,
    val maxZoom: Int,
    val serviceUrl: String,
    val matrixSet: String,
    val layer: String,
    val format: String,
    val style: String,
    val otherParams: List<Pair<String, String>> = emptyList(),
) : MapSettings
