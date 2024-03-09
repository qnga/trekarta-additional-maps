package org.qnga.trekarta.maps.ui.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.R
import org.qnga.trekarta.maps.ui.components.FormRow
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
        FormSection(title = stringResource(R.string.wmts_kvp_form_metadata)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settingsEditor.title.value,
                onValueChange = { settingsEditor.title.value = it },
                label = { Text(text = stringResource(R.string.wmts_kvp_form_title)) }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settingsEditor.provider.value,
                onValueChange = { settingsEditor.provider.value = it },
                label = { Text(text = stringResource(R.string.wmts_kvp_form_provider)) }
            )

            FormRow {

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.minZoom.value,
                    onValueChange = { settingsEditor.minZoom.value = it },
                    label = { Text(text = stringResource(R.string.wmts_kvp_form_min_zoom)) }
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.maxZoom.value,
                    onValueChange = { settingsEditor.maxZoom.value = it },
                    label = { Text(text = stringResource(R.string.wmts_kvp_form_max_zoom)) }
                )
            }
        }

        FormSection(title = stringResource(R.string.wmts_kvp_form_access)) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settingsEditor.serviceUrl.value,
                onValueChange = { settingsEditor.serviceUrl.value = it },
                label = { Text(text = stringResource(R.string.wmts_kvp_form_service_url)) }
            )

            FormRow {

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.matrixSet.value,
                    onValueChange = { settingsEditor.matrixSet.value = it },
                    label = { Text(text = stringResource(R.string.wmts_kvp_form_tilematrixset)) }
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.layer.value,
                    onValueChange = { settingsEditor.layer.value = it },
                    label = { Text(text = stringResource(R.string.wmts_kvp_form_layer)) }
                )
            }

            FormRow {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.style.value,
                    onValueChange = { settingsEditor.style.value = it },
                    label = { Text(text = stringResource(R.string.wmts_kvp_form_style)) }
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = settingsEditor.format.value,
                    onValueChange = { settingsEditor.format.value = it },
                    label = { Text(text = stringResource(R.string.wmts_kvp_form_format)) }
                )
            }
        }

        FormSection(
            title = stringResource(R.string.wmts_kvp_form_additional_parameters),
            isLastOne = true,
            afterTitle = {
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
                        contentDescription = stringResource(R.string.wmts_kvp_form_add_parameter_content_description)
                    )
                }
            }
        ) {
            settingsEditor.otherParams.value.forEachIndexed { index, (key, value) ->
                FormRow {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = key,
                        onValueChange = {
                            settingsEditor.otherParams.value = buildList {
                                addAll(settingsEditor.otherParams.value)
                                set(index, it to value)
                            }
                        },
                        label = { Text(text = stringResource(R.string.wmts_kvp_form_parameter_key)) }
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
                        label = { Text(text = stringResource(R.string.wmts_kvp_form_parameter_value)) }
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
                            contentDescription = stringResource(R.string.wmts_kvp_form_remove_parameter_content_description)
                        )
                    }
                }
            }
        }
    }
}
