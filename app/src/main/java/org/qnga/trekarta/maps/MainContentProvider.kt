package org.qnga.trekarta.maps

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.qnga.trekarta.maps.provider.MapProviderContract
import org.qnga.trekarta.maps.provider.ProviderQueryHandler

class MainContentProvider : ContentProvider() {

    private val coroutineScope: CoroutineScope =
        MainScope()

    private lateinit var queryHandler: Deferred<ProviderQueryHandler>

    override fun onCreate(): Boolean {
        val application =
            requireNotNull(context).applicationContext as MainApplication

        queryHandler = coroutineScope.async {
            application.onCreatedCompleted
                .filter { it }
                .first()

            val maps = application.mapRepository.maps
                .first()

            ProviderQueryHandler(maps)
        }

        return true
    }

    override fun query(
        query: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor = runBlocking {
        queryHandler.await().query(query)
    }

    override fun getType(uri: Uri): String {
        return MapProviderContract.TILE_TYPE // Multiple rows are not supported
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
