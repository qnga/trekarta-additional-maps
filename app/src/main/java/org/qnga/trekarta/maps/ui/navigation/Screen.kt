package org.qnga.trekarta.maps.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.core.maps.BritainOsOutdoorProvider
import org.qnga.trekarta.maps.core.maps.BritainOsRoadProvider
import org.qnga.trekarta.maps.core.maps.CustomWmtsKvpSettings
import org.qnga.trekarta.maps.core.maps.FranceIgnScan25Provider
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.core.maps.SpainIgnMtnProvider
import org.qnga.trekarta.maps.ui.maps.BritainOsOutdoorSettingsEditor
import org.qnga.trekarta.maps.ui.maps.BritainOsRoadSettingsEditor
import org.qnga.trekarta.maps.ui.maps.CustomWmtsKvpSettingsEditor
import org.qnga.trekarta.maps.ui.maps.FranceIgnScan25SettingsEditor
import org.qnga.trekarta.maps.ui.maps.MapSettingsEditor
import org.qnga.trekarta.maps.ui.maps.SpainIgnMtnSettingsEditor
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

  class MapDetails(
    private val settingsEditor: MapSettingsEditor,
    private val listener: MapDetailsListener
  ) : Screen() {

    constructor(
      mapProvider: MapProvider,
      listener: MapDetailsListener
    ) : this(
      settingsEditor = when (mapProvider) {
        is FranceIgnScan25Provider ->
          FranceIgnScan25SettingsEditor()
        is SpainIgnMtnProvider ->
          SpainIgnMtnSettingsEditor()
        is BritainOsOutdoorProvider ->
          BritainOsOutdoorSettingsEditor()
        is BritainOsRoadProvider ->
          BritainOsRoadSettingsEditor()
      },
      listener = listener
    )

    constructor(
      mapSettings: MapSettings,
      listener: MapDetailsListener
    ) : this(
      settingsEditor = when (mapSettings) {
        is FranceIgnScan25Provider.Settings ->
          FranceIgnScan25SettingsEditor(mapSettings)
        is SpainIgnMtnProvider.Settings ->
          SpainIgnMtnSettingsEditor(mapSettings)
        is BritainOsOutdoorProvider.Settings ->
          BritainOsOutdoorSettingsEditor(mapSettings)
        is BritainOsRoadProvider.Settings ->
          BritainOsRoadSettingsEditor(mapSettings)
        is CustomWmtsKvpSettings ->
          CustomWmtsKvpSettingsEditor(mapSettings)
      },
      listener = listener
    )

    @Composable
    override fun Screen() {
      MapDetailsScreen(settingsEditor, listener)
    }
  }
}
