package org.qnga.trekarta.maps

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainApplication : Application() {

    internal lateinit var mapRepository: MapRepository

    /*
     * This is useful because ContentProvider.onCreate is called
     * before Application.onCreate has finished.
     */
    val onCreatedCompleted: StateFlow<Boolean>
        get() = onCreateCompletedMutable

    private val onCreateCompletedMutable: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    override fun onCreate() {
        super.onCreate()

        mapRepository = MapRepository(this)

        onCreateCompletedMutable.value = true
    }
}