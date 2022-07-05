package org.qnga.trekarta.maps.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.qnga.trekarta.maps.MapRepository
import org.qnga.trekarta.maps.catalog.MapProvider
import org.qnga.trekarta.maps.catalog.OpenAccessMapProvider
import org.qnga.trekarta.maps.catalog.TokenAccessMapProvider
import org.qnga.trekarta.maps.ui.navigation.Backstack
import org.qnga.trekarta.maps.ui.navigation.Screen
import org.qnga.trekarta.maps.ui.screens.ProviderCatalogListener
import org.qnga.trekarta.maps.ui.screens.ProviderDetailListener

internal class MainViewModel(
    private val mapRepository: MapRepository
) : ViewModel(), ProviderCatalogListener, ProviderDetailListener {

    private lateinit var maps: Map<String, MapRepository.MapHolder>

    private lateinit var tokens: Map<String, String>

    init {
        viewModelScope.launch {
            maps = mapRepository.maps.first()
            Log.v(MainViewModel::class.java.name, "Maps initialized")

            tokens = mapRepository.tokens.first()
            Log.v(MainViewModel::class.java.name, "Tokens initialized")

            onProviderRepositoryReady(mapRepository.providers)
        }

        mapRepository.tokens
            .onEach { tokens = it }
            .launchIn(viewModelScope)

        mapRepository.maps
            .onEach { maps = it }
            .launchIn(viewModelScope)
    }

    private val backstack: Backstack<Screen> =
        Backstack(Screen.Loading)

    private fun onProviderRepositoryReady(providers: List<MapProvider>) {
        val catalogScreen = Screen.ProviderCatalog(providers, this)
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


    override fun onProviderClicked(provider: MapProvider) {
        return when (provider) {
            is OpenAccessMapProvider -> {
                // Do nothing
            }
            is TokenAccessMapProvider -> {
                val token = tokens[provider.identifier].orEmpty()
                val detailScreen = Screen.ProviderDetail(provider, token, this)
                backstack.add(detailScreen)
            }
        }
    }

    override fun onTokenSubmitted(provider: MapProvider, token: String?) {
        mapRepository.setAccessToken(provider.identifier, token)
    }

    class Factory(
        private val mapRepository: MapRepository
    ): ViewModelProvider.NewInstanceFactory() {

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