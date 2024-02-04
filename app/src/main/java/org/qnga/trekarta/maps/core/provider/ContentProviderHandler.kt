package org.qnga.trekarta.maps.core.provider

import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import org.qnga.trekarta.maps.core.handlers.Handler

class ContentProviderHandler(
    private val handlers: Map<String, Handler>
) {
    fun query(query: Uri): Cursor {
        return when (query.pathSegments[0]) {
            "maps" -> handleMapsQuery()
            "tiles" -> handleTileQuery(query)
            else -> MatrixCursor(emptyArray())
        }
    }

    private fun handleMapsQuery(): Cursor {
        val cursor = MatrixCursor(ContentProviderContract.MAPS_COLUMNS, 1)

        for (map in handlers.values) {
            cursor.addRow(
                arrayOf(
                    map.title,
                    map.id,
                    map.minZoom,
                    map.maxZoom
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

        val map = handlers[mapId]
            ?: return MatrixCursor(emptyArray())

        val tileURL = map.tile(zoomLevel, tileX, tileY)

        return TileCursor(tileURL)
    }
}
