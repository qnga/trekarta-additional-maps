package org.qnga.carteigntrekarta.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.qnga.carteigntrekarta.MainApplication

class IgnContentProvider : ContentProvider() {

    private val coroutineScope: CoroutineScope = MainScope()

    private var tileSource: WmtsTileSource? = null

    override fun onCreate(): Boolean {
        val application = requireNotNull(context).applicationContext as MainApplication

        application.key
            .onEach { updateTileSource(it) }
            .launchIn(coroutineScope)

        return true
    }

    private fun updateTileSource(key: String?) {
        tileSource =
            if (key == null) {
                null
            } else {
                val baseUrl = "https://wxs.ign.fr/$key/geoportail/wmts"
                WmtsTileSource(baseUrl)
            }
    }

    override fun query(
        query: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
       return when (query.pathSegments[0]) {
           "maps" -> handleMapsQuery(query)
           "tiles" -> handleTileQuery(query)
           else -> MatrixCursor(emptyArray())
       }
    }

    private fun handleMapsQuery(query: Uri): Cursor {
        return MatrixCursor(arrayOf("name", "identifier"), 1)
            .apply {
                addRow(arrayOf("Cartes IGN", "ign-fr"))
                addRow(arrayOf("Cartografía del IGN", "ign-es"))
            }
    }

    private fun handleTileQuery(query: Uri): Cursor {
        val segments = query.pathSegments
        val map = segments[1]
        val zoomLevel = segments[2]
        val tileX = requireNotNull(segments[3].toIntOrNull())
        val tileY = requireNotNull(segments[4].toIntOrNull())

        return when (map) {
            "ign-fr" -> handleIgnFranceQuery(zoomLevel, tileX, tileY)
            "ign-es" -> handleIgnEspagneQuery(zoomLevel, tileX, tileY)
            else -> MatrixCursor(emptyArray())
        }
    }

    private fun handleIgnFranceQuery(zoomLevel: String, tileX: Int, tileY: Int): Cursor {
        val tileSourceNow = tileSource ?: return MatrixCursor(emptyArray())

        val description = WmtsTileSource.TileDescription(
            matrix = zoomLevel,
            matrixSet = "PM",
            style = "normal",
            row = tileY,
            column = tileX,
            layer = "GEOGRAPHICALGRIDSYSTEMS.MAPS",
            format = "image/jpeg"
        )

        val tileUri = tileSourceNow.urlForTile(description)
        return TileSourceCursor(tileUri)
    }

    private fun handleIgnEspagneQuery(zoomLevel: String, tileX: Int, tileY: Int): Cursor {
        val description = WmtsTileSource.TileDescription(
            matrix = zoomLevel,
            matrixSet = "GoogleMapsCompatible",
            style = "default",
            row = tileY,
            column = tileX,
            layer = "MTN",
            format = "image/jpeg"
        )

        val tileUri = WmtsTileSource("https://www.ign.es/wmts/mapa-raster").urlForTile(description)
        return TileSourceCursor(tileUri)
    }

    override fun getType(uri: Uri): String {
        return TileSourceProviderContract.TILE_TYPE // Multiple rows are not supported
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null // Insert is not supported
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0 // Delete is not supported
    }

    override fun update(uri: Uri, values: ContentValues?, extras: String?, p3: Array<out String>?): Int {
        return 0 // Update is not supported
    }
}
