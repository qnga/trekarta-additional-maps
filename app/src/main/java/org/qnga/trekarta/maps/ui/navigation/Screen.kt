package org.qnga.trekarta.maps.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.ui.screens.LoadingScreen
import org.qnga.trekarta.maps.ui.screens.MapRegistryListener
import org.qnga.trekarta.maps.ui.screens.MapRegistryScreen
import org.qnga.trekarta.maps.ui.screens.UserMapsListener
import org.qnga.trekarta.maps.ui.screens.UserMapsScreen
import org.qnga.trekarta.maps.ui.screens.MapDetailsListener
import org.qnga.trekarta.maps.ui.screens.MapDetailsScreen

internal sealed class Screen {

  @SuppressLint("NotConstructor")
  @Composable
  abstract fun Screen()

  object Loading : Screen() {

    @Composable
    override fun Screen() {
      LoadingScreen()
    }
  }

  class UserMaps(
    private val maps: StateFlow<List<Map>>,
    private val listener: UserMapsListener
  ) : Screen() {

    @Composable
    override fun Screen() {
      UserMapsScreen(maps, listener)
    }
  }

  class MapRegistry(
    private val providers: StateFlow<List<MapProvider>>,
    private val listener: MapRegistryListener
  ) : Screen() {

    @Composable
    override fun Screen() {
      MapRegistryScreen(providers, listener)
    }
  }

  class MapDetailsForProvider(
    private val provider: MapProvider,
    private val listener: MapDetailsListener
  ) : Screen() {

    @Composable
    override fun Screen() {
      MapDetailsScreen(provider, listener)
    }
  }

  class MapDetailsForSettings(
    private val settings: MapSettings,
    private val listener: MapDetailsListener
  ) : Screen() {

    @Composable
    override fun Screen() {
      MapDetailsScreen(settings, listener)
    }
  }
}
