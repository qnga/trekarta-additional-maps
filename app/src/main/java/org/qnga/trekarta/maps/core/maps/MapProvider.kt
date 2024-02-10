package org.qnga.trekarta.maps.core.maps

import kotlinx.serialization.Serializable

sealed interface MapProvider {

    val id: String

    val title: String

    val provider: String
}

@Serializable
sealed interface MapSettings
