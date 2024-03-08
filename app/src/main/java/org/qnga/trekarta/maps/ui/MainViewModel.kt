package org.qnga.trekarta.maps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.core.data.MapRepository
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.ui.maps.CustomWmtsKvpSettingsEditor
import org.qnga.trekarta.maps.ui.screens.CustomMapSelectionListener
import org.qnga.trekarta.maps.ui.screens.MapDetailsListener
import org.qnga.trekarta.maps.ui.screens.MapRegistryListener
import org.qnga.trekarta.maps.ui.screens.UserMapsListener
import org.qnga.trekarta.maps.ui.util.Backstack

internal class MainViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val userMapsListener: UserMapsListener = object : UserMapsListener {

        override fun onMapActivated(map: Map) {
            val detailsScreen = Screens.MapDetails(map.settings, mapDetailsListener)
            backstack.add(detailsScreen)
        }

        override fun onMapRegistrySelected() {
            viewModelScope.launch {
                val registryScreen = Screens.MapRegistry(mapRepository.unusedProviders, mapRegistryListener)
                backstack.add(registryScreen)
            }
        }

        override fun onCustomMapSelected() {
            val customMapSelectionScreen = Screens.CustomMapSelection(customMapSelectionListener)
            backstack.add(customMapSelectionScreen)
        }
    }

    private val mapRegistryListener: MapRegistryListener = object : MapRegistryListener {

        override fun onProviderClicked(provider: MapProvider) {
            val settingsScreen = Screens.MapDetails(provider, mapDetailsListener)
            backstack.add(settingsScreen)
        }

        override fun onBackClicked() {
            backstack.pop()
        }
    }

    private val mapDetailsListener = object : MapDetailsListener {
        override fun onDoneClicked(settings: MapSettings) {
            mapRepository.upsertMap(settings)
            backstack.pop()
            backstack.pop()
        }

        override fun onDeleteClicked(settings: MapSettings) {
            mapRepository.removeMap(settings)
            backstack.pop()
        }

        override fun onBackClicked() {
            backstack.pop()
        }
    }

    private val customMapSelectionListener = object : CustomMapSelectionListener {
        override fun onWmtsKvpSelected() {
            val customMapScreen = Screens.MapDetails(CustomWmtsKvpSettingsEditor(), mapDetailsListener)
            backstack.add(customMapScreen)
        }

        override fun onBackClicked() {
            backstack.pop()
        }
    }

    private val backstack: Backstack<Screens> =
        Backstack(Screens.UserMaps(mapRepository.userMaps, userMapsListener))

    val currentScreen: StateFlow<Screens>
        get() = backstack.current

    fun onBackstackPressed(): Boolean {
        return if (backstack.size > 1) {
            backstack.pop()
            false
        } else {
            onCloseActivity()
            true
        }
    }

    private fun onCloseActivity() {}

    class Factory(
        private val mapRepository: MapRepository
    ): ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(mapRepository) as T
                else ->
                    super.create(modelClass)
            }
        }
    }
}
