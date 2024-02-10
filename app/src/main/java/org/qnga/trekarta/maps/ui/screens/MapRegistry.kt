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
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.ui.components.BackButton
import org.qnga.trekarta.maps.ui.components.TopBarTitle

interface MapRegistryListener {

    fun onProviderClicked(provider: MapProvider)

    fun onBackClicked()
}

@Composable
fun MapRegistryScreen(
    providers: StateFlow<List<MapProvider>>,
    listener: MapRegistryListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { TopBarTitle("Add map from registry") },
                navigationIcon = { BackButton(onClick = { listener.onBackClicked() }) }
            )
        }
    ) { innerPadding ->
        Column {
            Text(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Please select a map"
            )

            Divider(thickness = 3.dp)

            MapProviderList(
                modifier = Modifier.padding(innerPadding),
                providers = providers.collectAsState(),
                onProviderActivated = listener::onProviderClicked
            )
        }
    }
}

@Composable
private fun MapProviderList(
    modifier: Modifier,
    providers: State<List<MapProvider>>,
    onProviderActivated: (MapProvider) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = providers.value,
            key = { it.id }
        ) {
            ProviderItem(
                modifier = Modifier.fillMaxWidth(),
                title = it.title,
                onClick = { onProviderActivated(it) }
            )
            Divider()
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
