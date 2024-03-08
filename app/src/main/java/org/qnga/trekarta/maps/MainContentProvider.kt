package org.qnga.trekarta.maps

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.core.provider.ContentProviderContract
import org.qnga.trekarta.maps.core.provider.ContentProviderHandler

class MainContentProvider : ContentProvider() {

    private val coroutineScope: CoroutineScope =
        MainScope()

    private val queryHandler: MutableSharedFlow<ContentProviderHandler> =
        MutableSharedFlow(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(): Boolean {
        val application =
            requireNotNull(context).applicationContext as MainApplication

        application.onCreateCompleted
            .filter { it }
            .flatMapLatest { application.mapRepository.userMaps }
            .onEach { queryHandler.emit(it.toQueryHandler()) }
            .launchIn(coroutineScope)

        return true
    }

    private fun List<Map>.toQueryHandler(): ContentProviderHandler = this
        .associateBy { it.id }
        .mapValues { it.value.createHandler() }
        .let { ContentProviderHandler((it)) }

    override fun query(
        query: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor = runBlocking {
        queryHandler.first().query(query)
    }

    override fun getType(uri: Uri): String {
        return ContentProviderContract.TILE_TYPE // Multiple rows are not supported
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
