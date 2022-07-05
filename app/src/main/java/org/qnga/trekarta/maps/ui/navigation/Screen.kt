package org.qnga.trekarta.maps.ui.navigation

import androidx.compose.runtime.Composable
import org.qnga.trekarta.maps.catalog.MapProvider
import org.qnga.trekarta.maps.ui.screens.ProviderCatalogListener
import org.qnga.trekarta.maps.ui.screens.ProviderCatalogScreen
import org.qnga.trekarta.maps.ui.screens.ProviderDetailListener
import org.qnga.trekarta.maps.ui.screens.ProviderDetailScreen

internal sealed class Screen {

  @Composable
  abstract fun Screen()

  object Loading : Screen() {

    @Composable
    override fun Screen() {}
  }

  class ProviderCatalog(
    private val providers: List<MapProvider>,
    private val listener: ProviderCatalogListener
  ) : Screen() {

    @Composable
    override fun Screen() {
      ProviderCatalogScreen(providers, listener)
    }
  }

  class ProviderDetail(
    private val provider: MapProvider,
    private val token: String,
    private val listener: ProviderDetailListener
  ) : Screen() {

    @Composable
    override fun Screen() {
      ProviderDetailScreen(provider, token, listener)
    }
  }
}
