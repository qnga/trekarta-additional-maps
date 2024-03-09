package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.ui.components.LoadingBox
import org.qnga.trekarta.maps.ui.components.RetryBox
import org.qnga.trekarta.maps.ui.components.TopBarTitle

interface UserMapsListener {

    fun onMapActivated(map: Map)

    fun onMapRegistrySelected()

    fun onCustomMapSelected()
}

sealed interface UserMapsScreenState {

    data object Loading : UserMapsScreenState

    data class Error(
        val continuation: CancellableContinuation<Boolean>
    ) : UserMapsScreenState

    data class MapsAvailable(
        val maps: List<Map>
    ) : UserMapsScreenState
}

@Composable
fun UserMapsScreen(
    maps: Flow<List<Map>>,
    listener: UserMapsListener
) {
    val state: MutableState<UserMapsScreenState> =
        remember { mutableStateOf(UserMapsScreenState.Loading) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(null) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            maps
                .retry { suspendCancellableCoroutine { state.value = UserMapsScreenState.Error(it) } }
                .onEach { state.value = UserMapsScreenState.MapsAvailable(it) }
                .collect()
        }
    }

    UserMapsScreen(
        state = state,
        listener = listener
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun UserMapsScreen(
    state: State<UserMapsScreenState>,
    listener: UserMapsListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopBarTitle(text = stringResource(R.string.user_maps_title))
                }
            )
        },
        floatingActionButton = {
            val showDialog = remember { mutableStateOf(false) }

            if (showDialog.value) {
                SelectMapSourceDialog(
                    onRegistrySelected = {
                        showDialog.value = false
                        listener.onMapRegistrySelected()
                    },
                    onCustomMapSelected = {
                        showDialog.value = false
                        listener.onCustomMapSelected()
                    },
                    onDismiss = {
                        showDialog.value = false
                    }
                )
            }

            FloatingActionButton(
                onClick = { showDialog.value = true },
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.user_maps_add_content_description))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            propagateMinConstraints = true
        ) {
            when (val stateNow = state.value) {
                is UserMapsScreenState.Error -> {
                    RetryBox(
                        message = stringResource(R.string.user_maps_error),
                        onRetry = { stateNow.continuation.resume(true, onCancellation = { }) }
                    )
                }
                UserMapsScreenState.Loading -> {
                    LoadingBox()
                }
                is UserMapsScreenState.MapsAvailable -> {
                    UserMapList(
                        maps = stateNow.maps,
                        onMapActivated = listener::onMapActivated
                    )
                }
            }
        }
    }
}

@Composable
private fun UserMapList(
    modifier: Modifier = Modifier,
    maps: List<Map>,
    onMapActivated: (Map) -> Unit
) {
    if (maps.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.user_maps_empty))
        }
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(
                items = maps,
                key = { it.id }
            ) {
                ProviderItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = it.title,
                    onClick = { onMapActivated(it) }
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
        Text(text = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectMapSourceDialog(
    onRegistrySelected: () -> Unit,
    onCustomMapSelected: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.padding(PaddingValues(all = 24.dp)),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Button(onClick = onRegistrySelected) {
                    Text(text = stringResource(R.string.user_maps_select_source_registry))
                }

                TextButton(onClick = onCustomMapSelected) {
                    Text(text = stringResource(R.string.user_maps_select_source_custom))
                }
            }
        }
    }
}
