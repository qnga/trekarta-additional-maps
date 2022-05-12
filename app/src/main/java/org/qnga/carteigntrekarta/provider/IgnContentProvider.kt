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
                val baseUrl = "https://wxs.ign.fr/$key/geoportail/wmts" //"https://www.ign.es/wmts/mapa-raster"
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
        val tileSourceNow = tileSource ?: return MatrixCursor(emptyArray())
        val segments = query.pathSegments
        val zoomLevel = segments[0]
        val tileX = requireNotNull(segments[1].toIntOrNull())
        val tileY = requireNotNull(segments[2].toIntOrNull())

        val description = WmtsTileSource.TileDescription(
            matrix = zoomLevel,
            matrixSet = "PM", //"GoogleMapsCompatible"
            style = "normal", //"default"
            row = tileY,
            column = tileX,
            layer = "GEOGRAPHICALGRIDSYSTEMS.MAPS", // "MTN"
            format = "image/jpeg"
        )

        val tileUri = tileSourceNow.urlForTile(description)
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
