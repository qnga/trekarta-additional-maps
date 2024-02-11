package org.qnga.trekarta.maps.ui.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.ui.components.FormSection


@Composable
fun CustomWmtsKvpDetailsForm(
    settingsEditor: CustomWmtsKvpSettingsEditor
) {
    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormSection(title = "Metadata") {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settingsEditor.title.value,
                onValueChange = { settingsEditor.title.value = it },
                label = { Text(text = "Title") }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settingsEditor.provider.value,
                onValueChange = { settingsEditor.provider.value = it },
                label = { Text(text = "Provider") }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.minZoom.value,
                    onValueChange = { settingsEditor.minZoom.value = it },
                    label = { Text(text = "Min Zoom") }
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.maxZoom.value,
                    onValueChange = { settingsEditor.maxZoom.value = it },
                    label = { Text(text = "Max Zoom") }
                )
            }
        }

        FormSection(title = "Access") {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settingsEditor.serviceUrl.value,
                onValueChange = { settingsEditor.serviceUrl.value = it },
                label = { Text(text = "Service URL") }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.matrixSet.value,
                    onValueChange = { settingsEditor.matrixSet.value = it },
                    label = { Text(text = "TileMatrixSet") }
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.layer.value,
                    onValueChange = { settingsEditor.layer.value = it },
                    label = { Text(text = "Layer") }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.style.value,
                    onValueChange = { settingsEditor.style.value = it },
                    label = { Text(text = "Style") }
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.format.value,
                    onValueChange = { settingsEditor.format.value = it },
                    label = { Text(text = "Format") }
                )
            }
        }

        FormSection(
            title = "Additional parameters",
            isLastOne = true,
            trailingIcon = {
                OutlinedIconButton(
                    modifier = Modifier
                        .size(20.dp),
                    onClick = {
                        settingsEditor.otherParams.value =
                            settingsEditor.otherParams.value.toMutableList() + ("" to "")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            }
        ) {
            settingsEditor.otherParams.value.forEachIndexed { index, (key, value) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = key,
                        onValueChange = {
                            settingsEditor.otherParams.value = buildList {
                                addAll(settingsEditor.otherParams.value)
                                set(index, it to value)
                            }
                        },
                        label = { Text(text = "Key") }
                    )

                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = value,
                        onValueChange = {
                            settingsEditor.otherParams.value = buildList {
                                addAll(settingsEditor.otherParams.value)
                                set(index, key to it)
                            }
                        },
                        label = { Text(text = "Value") }
                    )

                    IconButton(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                        onClick = {
                            settingsEditor.otherParams.value = buildList {
                                addAll(settingsEditor.otherParams.value)
                                removeAt(index)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove"
                        )
                    }
                }
            }
        }
    }
}
