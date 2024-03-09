package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.suspendCancellableCoroutine
import org.qnga.trekarta.maps.R
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.ui.components.BackButton
import org.qnga.trekarta.maps.ui.components.LoadingBox
import org.qnga.trekarta.maps.ui.components.RetryBox
import org.qnga.trekarta.maps.ui.components.TopBarTitle

interface MapRegistryListener {

    fun onProviderClicked(provider: MapProvider)

    fun onBackClicked()
}

sealed interface MapRegistryScreenState {

    data object Loading : MapRegistryScreenState

    data class Error(
        val continuation: CancellableContinuation<Boolean>
    ) : MapRegistryScreenState

    data class ProvidersAvailable(
        val providers: List<MapProvider>
    ) : MapRegistryScreenState
}

@Composable
fun MapRegistryScreen(
    providers: Flow<List<MapProvider>>,
    listener: MapRegistryListener
) {
    val state: MutableState<MapRegistryScreenState> =
        remember { mutableStateOf(MapRegistryScreenState.Loading) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(null) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            providers
                .retry { suspendCancellableCoroutine { state.value = MapRegistryScreenState.Error(it) } }
                .onEach { state.value = MapRegistryScreenState.ProvidersAvailable(it) }
                .collect()
        }
    }

    MapRegistryScreen(
        state = state,
        listener = listener
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun MapRegistryScreen(
    state: State<MapRegistryScreenState>,
    listener: MapRegistryListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { TopBarTitle(text = stringResource(R.string.map_registry_title)) },
                navigationIcon = { BackButton(onClick = { listener.onBackClicked() }) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HorizontalDivider(thickness = 3.dp)

            when (val stateNow = state.value) {
                is MapRegistryScreenState.Error -> {
                    RetryBox(
                        message = stringResource(R.string.map_registry_error),
                        onRetry = { stateNow.continuation.resume(true, onCancellation = { }) }
                    )
                }
                is MapRegistryScreenState.ProvidersAvailable -> {
                    MapProviderList(
                        providers = stateNow.providers,
                        onProviderActivated = listener::onProviderClicked
                    )
                }
                MapRegistryScreenState.Loading -> {
                    LoadingBox()
                }
            }
        }
    }
}

@Composable
private fun MapProviderList(
    modifier: Modifier = Modifier,
    providers: List<MapProvider>,
    onProviderActivated: (MapProvider) -> Unit
) {
    if (providers.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.map_registry_empty))
        }
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(
                items = providers,
                key = { it.id }
            ) {
                ProviderItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = it.title,
                    onClick = { onProviderActivated(it) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun ProviderItem(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() }
            .padding(15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(title)
    }
}
