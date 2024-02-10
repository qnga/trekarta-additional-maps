package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.ui.components.BackButton
import org.qnga.trekarta.maps.ui.components.DeleteButton
import org.qnga.trekarta.maps.ui.components.DoneButton
import org.qnga.trekarta.maps.ui.components.TopBarTitle
import org.qnga.trekarta.maps.ui.maps.MapDetailsForm
import org.qnga.trekarta.maps.ui.maps.MapSettingsEditor

interface MapDetailsListener {

    fun onDoneClicked(settings: MapSettings)

    fun onDeleteClicked(settings: MapSettings)

    fun onBackClicked()
}

@Composable
fun MapDetailsScreen(
    settingsEditor: MapSettingsEditor,
    listener: MapDetailsListener
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (settingsEditor.initialSettings == null ||
                settingsEditor.currentSettings.value != settingsEditor.initialSettings) {
                MapDetailsEditTopBar(
                    isAdding = settingsEditor.initialSettings == null,
                    doneEnabled = settingsEditor.currentSettings.value != null,
                    onDoneClicked = {
                        listener.onDoneClicked(
                            checkNotNull(settingsEditor.currentSettings.value)
                        )
                    },
                    onBackClicked = { listener.onBackClicked() }
                )
            } else {
                MapDetailsViewTopBar(
                    onDeleteClicked = {
                        listener.onDeleteClicked(
                            checkNotNull(settingsEditor.initialSettings)
                        )
                    },
                    onBackClicked = { listener.onBackClicked() }
                )
            }
        }
    ) { innerPadding ->
        MapDetailsBox(
            innerPadding = innerPadding
        ) {
            MapDetailsForm(
                settingsEditor
            )
        }
    }
}

@Composable
private fun MapDetailsBox(
    innerPadding: PaddingValues,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp)
            .fillMaxSize()
    ) {
        content()
    }
}

@Composable
private fun MapDetailsViewTopBar(
    onDeleteClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = { TopBarTitle(text = "Edit map") },
        navigationIcon = { BackButton(onClick = onBackClicked) },
        actions = { DeleteButton(onClick = onDeleteClicked) }
    )
}

@Composable
private fun MapDetailsEditTopBar(
    isAdding: Boolean,
    doneEnabled: Boolean,
    onDoneClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = { TopBarTitle(text = if (isAdding) "Add map" else "Edit map") },
        navigationIcon = { BackButton(onClick = onBackClicked) },
        actions = {
            DoneButton(
                enabled = doneEnabled,
                onClick = onDoneClicked,
            )
        }
    )
}