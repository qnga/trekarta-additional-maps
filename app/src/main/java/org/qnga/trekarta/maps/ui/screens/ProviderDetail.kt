package org.qnga.trekarta.maps.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import org.qnga.trekarta.maps.catalog.FranceIgnMapProvider
import org.qnga.trekarta.maps.catalog.MapProvider

interface ProviderDetailListener {

    fun onTokenSubmitted(provider: MapProvider, token: String?)
}

@Composable
fun ProviderDetailScreen(
    provider: MapProvider,
    token: String,
    listener: ProviderDetailListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ProviderDetail(
            modifier = Modifier.padding(innerPadding),
            provider = provider,
            token = token,
            onTokenSubmitted = { listener.onTokenSubmitted(provider, it) }
        )
    }
}

@Composable
private fun ProviderDetail(
    modifier: Modifier,
    provider: MapProvider,
    token: String,
    onTokenSubmitted: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        TokenForm(
            Modifier.padding(5.dp),
            token,
            onTokenSubmitted
        )
        if(provider is FranceIgnMapProvider) {
            IgnKeyInfo()
        }
    }
}

@Composable
private fun TokenForm(
    modifier: Modifier,
    initialKey: String,
    onTokenSubmitted: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        val tokenValue : MutableState<String?> = remember { mutableStateOf(null) }
        Text(
            text = stringResource(R.string.IGN_key_label),
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        val focusManager = LocalFocusManager.current

        TextField(
            value = tokenValue.value ?: initialKey,
            onValueChange = { tokenValue.value = it },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    tokenValue.value
                        ?.replace("\\s".toRegex(), "")
                        ?.let { onTokenSubmitted(it) }
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
