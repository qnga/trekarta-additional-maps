package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.catalog.MapProvider

interface ProviderCatalogListener {

    fun onProviderClicked(provider: MapProvider)
}

@Composable
fun ProviderCatalogScreen(
    providers: List<MapProvider>,
    listener: ProviderCatalogListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ProviderList(
            modifier = Modifier.padding(innerPadding),
            providers = providers,
            onProviderClicked = listener::onProviderClicked
        )
    }
}

@Composable
fun ProviderList(
    modifier: Modifier,
    providers: List<MapProvider>,
    onProviderClicked: (MapProvider) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
            items(providers) {
                ProviderItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = it.title,
                    onClick = { onProviderClicked(it) }
                )
                Divider()
            }
    }
}

@Composable
fun ProviderItem(
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
