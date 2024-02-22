package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.ui.components.BackButton
import org.qnga.trekarta.maps.ui.components.TopBarTitle

interface CustomMapSelectionListener {

    fun onWmtsKvpSelected()

    fun onBackClicked()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMapSelectionScreen(
    listener: CustomMapSelectionListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { TopBarTitle("Add a custom map") },
                navigationIcon = { BackButton(onClick = { listener.onBackClicked() }) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WarningSection(
                modifier = Modifier.padding(20.dp)
            )

            ChoiceButtons(
                onWmtsKvpSelected = listener::onWmtsKvpSelected
            )
        }
    }
}

@Composable
private fun WarningSection(
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Image(
            Icons.Filled.Warning,
            modifier = Modifier.size(50.dp),
            alignment = Alignment.Center,
            contentDescription = "Warning"
        )

        Text(
            text = "Trekarta supporte seulement les cartes conformes à un certain nombre de conventions " +
                    "largement répandues sur le Web et compatibles avec OpenStreetMap et Google Maps.",
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun ChoiceButtons(
    onWmtsKvpSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        TextButton(onClick = onWmtsKvpSelected) {
            Text("Add a WMTS map with KVP encoding")
        }
    }
}
