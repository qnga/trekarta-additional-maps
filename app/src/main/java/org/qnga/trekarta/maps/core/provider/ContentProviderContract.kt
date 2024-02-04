package org.qnga.trekarta.maps.core.provider

object ContentProviderContract {

    const val TILE_TYPE = "vnd.android.cursor.item/vnd.mobi.maptrek.maps.xml.online.provider.tile"

    val TILE_COLUMNS = arrayOf("TILE")

    val MAPS_COLUMNS = arrayOf("NAME", "IDENTIFIER", "MIN_ZOOM", "MAX_ZOOM")
}
