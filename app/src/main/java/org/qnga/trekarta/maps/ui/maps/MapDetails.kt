package org.qnga.trekarta.maps.ui.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.R

@Composable
fun MapDetailsForm(
    settingsEditor: MapSettingsEditor
) {
    when (settingsEditor) {
        is SpainIgnMtnSettingsEditor ->
            SpainIgnMtnDetailsForm()
        is FranceIgnScan25SettingsEditor ->
            FranceIgnScan25DetailsForm(settingsEditor)
        is BritainOsRoadSettingsEditor ->
            BritainOsRoadDetailsForm(settingsEditor)
        is BritainOsOutdoorSettingsEditor ->
            BritainOsOutdoorDetailsForm(settingsEditor)
        is CustomWmtsKvpSettingsEditor ->
            CustomWmtsKvpDetailsForm(settingsEditor)
    }
}

@Composable
private fun SpainIgnMtnDetailsForm() {
}

@Composable
private fun FranceIgnScan25DetailsForm(
    settingsEditor: FranceIgnScan25SettingsEditor
) {
    AuthenticatedProviderForm(
        settingsEditor.token,
        providerDetails = { FranceIgnScan25KeyInfo() }
    )
}

@Composable
private fun BritainOsOutdoorDetailsForm(
    settingsEditor: BritainOsOutdoorSettingsEditor
) {
    AuthenticatedProviderForm(
        settingsEditor.token,
        providerDetails = { }
    )
}

@Composable
private fun BritainOsRoadDetailsForm(
    settingsEditor: BritainOsRoadSettingsEditor
) {
    AuthenticatedProviderForm(
        token = settingsEditor.token,
        providerDetails = { }
    )
}

@Composable
private fun AuthenticatedProviderForm(
    token: MutableState<String>,
    providerDetails: @Composable () -> Unit
) {
   AuthenticatedProviderForm(
       token = token.value,
       providerDetails = providerDetails,
       onTokenChanged = { token.value = it }
   )
}


@Composable
private fun AuthenticatedProviderForm(
    token: String,
    providerDetails: @Composable () -> Unit,
    onTokenChanged: (String) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        TokenForm(
            modifier = Modifier.padding(5.dp),
            token = token,
            onTokenChanged = onTokenChanged
        )

        providerDetails()
    }
}

@Composable
private fun TokenForm(
    modifier: Modifier,
    token: String,
    onTokenChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.IGN_key_label),
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        val focusManager = LocalFocusManager.current
        val showErrors = remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier.onFocusChanged { if (it.isFocused) showErrors.value = true },
            value = token,
            onValueChange = {
                onTokenChanged(it)
            },
            singleLine = true,
            isError = showErrors.value && token.isBlank(),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@Composable
private fun FranceIgnScan25KeyInfo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(5.dp)
    ) {
        ProvideTextStyle(value = TextStyle(textAlign = TextAlign.Justify)) {
            Text(
                text = stringResource(R.string.IGN_key_info_header),
            )
            Text(
                text = stringResource(R.string.IGN_key_step1),
            )
            Text(
                text = stringResource(R.string.IGN_key_step2),
            )
        }
    }
}
