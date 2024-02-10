package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import org.qnga.trekarta.maps.core.data.Map
import org.qnga.trekarta.maps.ui.components.AppNameTitle

interface UserMapsListener {

    fun onMapActivated(map: Map)

    fun onAddMap()
}

@Composable
fun UserMapsScreen(
    maps: StateFlow<List<Map>>,
    listener: UserMapsListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { AppNameTitle() })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { listener.onAddMap() },
                shape = CircleShape,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, "Add a map")
            }
        }
    ) { innerPadding ->
        UserMapList(
            modifier = Modifier.padding(innerPadding),
            maps = maps.collectAsState(),
            onMapActivated = listener::onMapActivated
        )
    }
}

@Composable
private fun UserMapList(
    modifier: Modifier,
    maps: State<List<Map>>,
    onMapActivated: (Map) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
            items(
                items = maps.value,
                key = { it.id }
            ) {
                ProviderItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = it.title,
                    onClick = { onMapActivated(it) }
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
