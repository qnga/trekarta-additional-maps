package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.R
import org.qnga.trekarta.maps.core.maps.BritainOsOutdoorProvider
import org.qnga.trekarta.maps.core.maps.BritainOsRoadProvider
import org.qnga.trekarta.maps.core.maps.CustomWmtsKvpSettings
import org.qnga.trekarta.maps.core.maps.FranceIgnScan25Provider
import org.qnga.trekarta.maps.core.maps.MapProvider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.core.maps.SpainIgnMtnProvider
import org.qnga.trekarta.maps.ui.components.AppTopBar

interface MapDetailsListener {

    fun onDetailsValidated(settings: MapSettings)

    fun onDetailsDismissed()
}

@Composable
fun MapDetailsScreen(
    currentSettings: MapSettings,
    listener: MapDetailsListener
) {
    when (currentSettings) {
        is SpainIgnMtnProvider.Settings ->
            SpainIgnMtnDetailsScreen(listener)
        is FranceIgnScan25Provider.Settings ->
            FranceIgnScan25DetailsScreen(currentSettings, listener)
        is BritainOsRoadProvider.Settings ->
            BritainOsRoadDetailsScreen(currentSettings, listener)
        is BritainOsOutdoorProvider.Settings ->
            BritainOsOutdoorDetailsScreen(currentSettings, listener)
        is CustomWmtsKvpSettings ->
            CustomWmtsKvpDetailsScreen()
    }
}

@Composable
fun MapDetailsScreen(
    mapProvider: MapProvider,
    listener: MapDetailsListener
) {
    when (mapProvider) {
        SpainIgnMtnProvider ->
            SpainIgnMtnDetailsScreen(listener)
        FranceIgnScan25Provider ->
            FranceIgnScan25DetailsScreen(currentSettings = null, listener)
        BritainOsRoadProvider ->
            BritainOsRoadDetailsScreen(currentSettings = null, listener)
        BritainOsOutdoorProvider ->
            BritainOsOutdoorDetailsScreen(currentSettings = null, listener)
    }
}

@Composable
private fun SpainIgnMtnDetailsScreen(
    listener: MapDetailsListener
) {
    MapDetailsScaffold(
        onAdd = { listener.onDetailsValidated(SpainIgnMtnProvider.Settings) },
        onCancel = { listener.onDetailsDismissed() }
    ) {}
}

@Composable
private fun FranceIgnScan25DetailsScreen(
    currentSettings: FranceIgnScan25Provider.Settings?,
    listener: MapDetailsListener
) {
    AuthenticatedProviderDetails(
        initialToken = currentSettings?.accessToken.orEmpty(),
        providerDetails = { IgnKeyInfo() },
        onValidate = { listener.onDetailsValidated(FranceIgnScan25Provider.Settings(it)) },
        onCancel = { listener.onDetailsDismissed() }
    )
}

@Composable
private fun BritainOsOutdoorDetailsScreen(
    currentSettings: BritainOsOutdoorProvider.Settings?,
    listener: MapDetailsListener
) {
    AuthenticatedProviderDetails(
        initialToken = currentSettings?.accessToken.orEmpty(),
        providerDetails = { },
        onValidate = { listener.onDetailsValidated(BritainOsOutdoorProvider.Settings(it)) },
        onCancel = { listener.onDetailsDismissed() }
    )
}

@Composable
private fun BritainOsRoadDetailsScreen(
    currentSettings: BritainOsRoadProvider.Settings?,
    listener: MapDetailsListener
) {
    AuthenticatedProviderDetails(
        initialToken = currentSettings?.accessToken.orEmpty(),
        providerDetails = { },
        onValidate = { listener.onDetailsValidated(BritainOsRoadProvider.Settings(it)) },
        onCancel = { listener.onDetailsDismissed() }
    )
}

@Composable
private fun CustomWmtsKvpDetailsScreen() {
}

@Composable
private fun MapDetailsScaffold(
    onAdd: () -> Unit,
    onCancel: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            content()
            MapDetailsButtons(
                modifier = Modifier.align(Alignment.BottomEnd),
                onAdd = onAdd,
                onCancel = onCancel
            )
        }
    }
}

@Composable
private fun MapDetailsButtons(
    modifier: Modifier,
    onAdd: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        TextButton(onClick = onCancel) {
            Text("Cancel")
        }
        
        Button(onClick = onAdd) {
            Text("OK")
        }
    }
}

@Composable
private fun AuthenticatedProviderDetails(
    initialToken: String,
    providerDetails: @Composable () -> Unit,
    onValidate: (String) -> Unit,
    onCancel: () -> Unit
) {
    val token : MutableState<String> = remember { mutableStateOf(initialToken) }

    MapDetailsScaffold(
        onAdd = {  onValidate(token.value.replace("\\s".toRegex(), "")) },
        onCancel = onCancel
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            TokenForm(
                modifier = Modifier.padding(5.dp),
                token = token
            )

            providerDetails()
        }
    }
}

@Composable
private fun TokenForm(
    modifier: Modifier,
    token: MutableState<String>
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

        TextField(
            value = token.value,
            onValueChange = { token.value = it },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@Composable
private fun IgnKeyInfo() {
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
