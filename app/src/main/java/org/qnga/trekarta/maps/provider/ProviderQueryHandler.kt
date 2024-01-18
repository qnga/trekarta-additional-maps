package org.qnga.trekarta.maps.provider

import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import org.qnga.trekarta.maps.MapRepository

internal class ProviderQueryHandler(
    private val mapHolders: Map<String, MapRepository.MapHolder>
) {
    fun query(query: Uri): Cursor {
        return when (query.pathSegments[0]) {
            "maps" -> handleMapsQuery()
            "tiles" -> handleTileQuery(query)
            else -> MatrixCursor(emptyArray())
        }
    }

    private fun handleMapsQuery(): Cursor {
        val cursor = MatrixCursor(arrayOf("name", "identifier", "min_zoom", "max_zoom"), 1)

        for (map in mapHolders.values) {
            cursor.addRow(
                arrayOf(
                    map.provider.title,
                    map.provider.identifier,
                    map.source.minZoom,
                    map.source.maxZoom
                )
            )
        }

        return cursor
    }

    private fun handleTileQuery(query: Uri): Cursor {
        val segments = query.pathSegments
        val mapId = segments[1]
        val zoomLevel = requireNotNull(segments[2].toIntOrNull())
        val tileX = requireNotNull(segments[3].toIntOrNull())
        val tileY = requireNotNull(segments[4].toIntOrNull())

        val map = mapHolders[mapId]
            ?: return MatrixCursor(emptyArray())

        val tileURL = map.source.tileUrl(zoomLevel, tileX, tileY)

        return MapCursor(tileURL)
    }
}
