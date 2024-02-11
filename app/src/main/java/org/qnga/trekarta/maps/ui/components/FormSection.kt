package org.qnga.trekarta.maps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormSection(
    title: String,
    isLastOne: Boolean = false,
    trailingIcon: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,

                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.width(15.dp))

            trailingIcon()
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            content()
        }
    }

    if (!isLastOne) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}
