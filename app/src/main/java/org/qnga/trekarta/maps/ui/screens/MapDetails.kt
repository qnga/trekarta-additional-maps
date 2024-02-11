package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.ui.components.BackButton
import org.qnga.trekarta.maps.ui.components.CloseButton
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
            when {
                settingsEditor.initialSettings == null -> {
                    val showAbandonAlert = remember { mutableStateOf(false) }

                    if (showAbandonAlert.value) {
                        ConfirmAbandonDialog(
                            onConfirmation = {
                                listener.onBackClicked()
                                showAbandonAlert.value = false
                            },
                            onCancel = { showAbandonAlert.value = false }
                        )
                    }

                    MapDetailsAddTopBar(
                        doneEnabled = settingsEditor.currentSettings.value != null,
                        onDoneClicked = {
                            listener.onDoneClicked(
                                checkNotNull(settingsEditor.currentSettings.value)
                            )
                        },
                        onBackClicked = { showAbandonAlert.value = true }
                    )
                }
                settingsEditor.currentSettings.value == settingsEditor.initialSettings -> {
                    val showRemoveDialog = remember { mutableStateOf(false) }

                    if (showRemoveDialog.value) {
                        ConfirmRemoveDialog(
                            onConfirmation = {
                                listener.onDeleteClicked(
                                    checkNotNull(settingsEditor.initialSettings)
                                )
                                showRemoveDialog.value = false
                            },
                            onCancel = { showRemoveDialog.value = false }
                        )
                    }

                    MapDetailsViewTopBar(
                        onDeleteClicked = {
                            showRemoveDialog.value = true
                        },
                        onBackClicked = { listener.onBackClicked() }
                    )
                }
                else -> {
                    val showDiscardChangesDialog = remember { mutableStateOf(false) }

                    if (showDiscardChangesDialog.value) {
                        ConfirmDiscardChangesDialog(
                            onConfirmation = {
                                settingsEditor.reset()
                                showDiscardChangesDialog.value = false
                            },
                            onCancel = { showDiscardChangesDialog.value = false }
                        )
                    }

                    MapDetailsEditTopBar(
                        doneEnabled = settingsEditor.currentSettings.value != null,
                        onDoneClicked = {
                            listener.onDoneClicked(
                                checkNotNull(settingsEditor.currentSettings.value)
                            )
                        },
                        onCloseClicked = { showDiscardChangesDialog.value = true }
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapDetailsViewTopBar(
    onDeleteClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { TopBarTitle(text = "Edit map") },
        navigationIcon = { BackButton(onClick = onBackClicked) },
        actions = {
            DeleteButton(onClick = onDeleteClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapDetailsEditTopBar(
    doneEnabled: Boolean,
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { TopBarTitle(text = "Edit map") },
        navigationIcon = {
            CloseButton(onClick = onCloseClicked)
                         },
        actions = {
            DoneButton(
                enabled = doneEnabled,
                onClick = onDoneClicked,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapDetailsAddTopBar(
    doneEnabled: Boolean,
    onDoneClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { TopBarTitle(text = "Add map") },
        navigationIcon = {
            BackButton(onClick = onBackClicked)
        },
        actions = {
            DoneButton(
                enabled = doneEnabled,
                onClick = onDoneClicked,
            )
        }
    )
}

@Composable
private fun ConfirmDialog(
    title: String,
    body: String,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = body) },
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
               Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(cancelText)
            }
        }
    )
}

@Composable
private fun ConfirmRemoveDialog(
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
) {
    ConfirmDialog(
        title = "Remove map?",
        body = "Settings will be lost.",
        confirmText = "Remove",
        onConfirmation = onConfirmation,
        onCancel = onCancel
    )
}

@Composable
private fun ConfirmDiscardChangesDialog(
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
) {
    ConfirmDialog(
        title = "Discard changes?",
        body = "Settings will be restored to their last saved state.",
        confirmText = "Discard",
        onConfirmation = onConfirmation,
        onCancel = onCancel
    )
}

@Composable
private fun ConfirmAbandonDialog(
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
) {
    ConfirmDialog(
        title = "Abandon?",
        body = "The map will not be added.",
        confirmText = "Abandon",
        onConfirmation = onConfirmation,
        onCancel = onCancel
    )
}