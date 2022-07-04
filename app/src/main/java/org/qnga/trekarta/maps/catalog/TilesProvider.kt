package org.qnga.trekarta.maps.catalog

interface TilesProvider {

    val identifier: String

    val title: String

    fun tileUrl(zoom: Int, x: Int, y: Int): String
}