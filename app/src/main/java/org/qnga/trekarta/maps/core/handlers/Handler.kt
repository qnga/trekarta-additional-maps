package org.qnga.trekarta.maps.core.handlers

interface Handler {

    val id: String

    val title: String

    val minZoom: Int

    val maxZoom: Int

    fun tile(zoom: Int, x: Int, y: Int): String
}

abstract class AbstractHandler(
    override val id: String,
    override val title: String,
    override val minZoom: Int,
    override val maxZoom: Int,
) : Handler
