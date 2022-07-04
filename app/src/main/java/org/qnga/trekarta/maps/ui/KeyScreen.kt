package org.qnga.trekarta.maps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ProvideTextStyle
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

@Composable
fun KeyScreen(
    initialKey: String,
    onKeySubmitted: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
       KeyBox(
           modifier = Modifier
               .padding(20.dp)
               .align(Alignment.Center),
           initialKey = initialKey,
           onKeySubmitted = onKeySubmitted
       )
    }
}

@Composable
fun KeyBox(
    modifier: Modifier,
    initialKey: String,
    onKeySubmitted: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        KeyForm(
            Modifier.padding(5.dp),
            initialKey,
            onKeySubmitted
        )
        IgnKeyInfo()
    }
}

@Composable
fun KeyForm(
    modifier: Modifier,
    initialKey: String,
    onKeySubmitted: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val keyValue : MutableState<String?> = remember { mutableStateOf(null) }
        Text(
            text = stringResource(R.string.IGN_key_label),
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        val focusManager = LocalFocusManager.current

        TextField(
            value = keyValue.value ?: initialKey,
            onValueChange = { keyValue.value = it },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyValue.value?.let { onKeySubmitted(it) }
                }
            )
        )
    }
}

@Composable
fun IgnKeyInfo() {
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
