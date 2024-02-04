package org.qnga.trekarta.maps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.core.data.MapRepository
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.ui.navigation.Backstack
import org.qnga.trekarta.maps.ui.navigation.Screen
import org.qnga.trekarta.maps.ui.screens.MapDetailsListener
import org.qnga.trekarta.maps.ui.screens.MapRegistryListener
import org.qnga.trekarta.maps.ui.screens.UserMapsListener

internal class MainViewModel(
    private val mapRepository: MapRepository
) : ViewModel(), UserMapsListener, MapDetailsListener, MapRegistryListener {

    private val backstack: Backstack<Screen> =
        Backstack(Screen.Loading)

    init {
        viewModelScope.launch {
            val maps = mapRepository.userMaps.stateIn(viewModelScope)
            onMapRepositoryReady(maps)
        }
    }

    private fun onMapRepositoryReady(maps: StateFlow<List<Map>>) {
        val catalogScreen = Screen.UserMaps(maps, this)
        backstack.replace(catalogScreen)
    }

    val currentScreen: StateFlow<Screen>
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


    override fun onMapActivated(map: Map) {
        val detailsScreen = Screen.MapDetailsForSettings(map.settings, this)
        backstack.add(detailsScreen)
    }

    override fun onDetailsValidated(settings: MapSettings) {
        mapRepository.replaceMap(settings)
        backstack.pop()
        backstack.pop()
    }

    override fun onDetailsDismissed() {
        backstack.pop()
        backstack.pop()
    }

    override fun onProviderClicked(provider: MapProvider) {
        val settingsScreen = Screen.MapDetailsForProvider(provider, this)
        backstack.add(settingsScreen)
    }

    override fun onAddMap() {
        viewModelScope.launch {
            val providers = mapRepository.unusedProviders.stateIn(viewModelScope)
            val registryScreen = Screen.MapRegistry(providers, this@MainViewModel)
            backstack.add(registryScreen)
        }
    }


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
