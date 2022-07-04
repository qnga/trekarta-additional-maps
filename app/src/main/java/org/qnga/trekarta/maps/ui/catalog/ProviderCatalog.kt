package org.qnga.trekarta.maps.ui.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

private val providers =
    listOf(
        "Carte IGN de la France",
        "Mapa IGN de España",
        "Ordnance Survey Road Map",
        "Ordnance Survey Outdoor Map"
    )

@Composable
fun ProviderList(
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
            items(providers) {
                ProviderItem(
                    modifier = Modifier,
                    title = it,
                    onClick = { }
                )
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
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        TextButton(
            onClick = onClick,
            content = { Text(title) }
        )
    }
}