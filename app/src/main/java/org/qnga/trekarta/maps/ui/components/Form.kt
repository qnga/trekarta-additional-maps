package org.qnga.trekarta.maps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
    afterTitle: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
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

            afterTitle()
        }

        Spacer(modifier = Modifier.height(12.dp))

        FormColumn {
            content()
        }
    }

    if (!isLastOne) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FormColumn(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FormItemSpacing)
    ) {
        content()
    }
}

@Composable
fun FormRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FormItemSpacing)
    ) {
        content()
    }
}

private val FormItemSpacing = 10.dp
