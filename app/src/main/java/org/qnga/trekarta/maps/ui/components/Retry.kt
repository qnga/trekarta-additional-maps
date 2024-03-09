package org.qnga.trekarta.maps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.R

@Composable
fun RetryBox(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier =  Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message
            )

            Button(
                onClick = onRetry
            ) {
                Text(text = stringResource(R.string.retrybox_retry))
            }
        }
    }
}
