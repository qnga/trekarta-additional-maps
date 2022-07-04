package org.qnga.trekarta.maps.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.qnga.trekarta.maps.MainApplication
import org.qnga.trekarta.maps.catalog.FranceIgnMap
import org.qnga.trekarta.maps.catalog.TilesProvider
import org.qnga.trekarta.maps.ui.theme.TrekartaAdditionalMaps


class MainActivity : ComponentActivity() {

    private val mainApplication by lazy { applicationContext as MainApplication }

    private val providerRepository by lazy { mainApplication.providerRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrekartaAdditionalMaps {
                val providers = providerRepository.providers
                    .map { it.values.toList() }
                    .collectAsState(initial = null)

                providers.value?.let {
                    Content(it, providerRepository::setFranceIgnMapToken)
                }
            }
        }
    }

    @Composable
    private fun Content(
        providers: List<TilesProvider>,
        onKeySubmitted: (String?) -> Unit
    ) {
        val franceIgnMap = derivedStateOf {
            providers
                .filterIsInstance(FranceIgnMap::class.java)
                .firstOrNull()
        }

        KeyScreen(
            initialKey = franceIgnMap.value?.accessToken.orEmpty(),
            onKeySubmitted = { token ->
                val validToken = token.takeUnless(String::isBlank)
                onKeySubmitted(validToken)
            }
        )
    }
}
