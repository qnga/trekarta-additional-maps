package org.qnga.trekarta.maps.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.qnga.trekarta.maps.MainApplication
import org.qnga.trekarta.maps.catalog.TilesProvider

class TrekartaContentProvider : ContentProvider() {

    private val coroutineScope: CoroutineScope =
        MainScope()

    private var providers: Map<String, TilesProvider> =
        emptyMap()

    override fun onCreate(): Boolean {
        val application =
            requireNotNull(context).applicationContext as MainApplication

        application.onCreatedCompleted
            .filter { it }
            .onEach { onApplicationStarted(application) }
            .launchIn(coroutineScope)

        return true
    }

    private fun onApplicationStarted(application: MainApplication) {
        application.providerRepository.providers
            .onEach { providers = it }
            .launchIn(coroutineScope)
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
        val cursor = MatrixCursor(arrayOf("name", "identifier"), 1)

        val providersNow = providers

        for (provider in providersNow.values) {
            cursor.addRow(arrayOf(provider.title, provider.identifier))
        }

        return cursor
    }

    private fun handleTileQuery(query: Uri): Cursor {
        val segments = query.pathSegments
        val map = segments[1]
        val zoomLevel = requireNotNull(segments[2].toIntOrNull())
        val tileX = requireNotNull(segments[3].toIntOrNull())
        val tileY = requireNotNull(segments[4].toIntOrNull())

        val provider = providers[map]
            ?: return MatrixCursor(emptyArray())

        val tileURL = provider.tileUrl(zoomLevel, tileX, tileY)

        return TileSourceCursor(tileURL)
    }

    override fun getType(uri: Uri): String {
        return TrekartaProviderContract.TILE_TYPE // Multiple rows are not supported
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
