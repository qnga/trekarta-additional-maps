package org.qnga.trekarta.maps.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import org.qnga.trekarta.maps.R

@Composable
fun TopBarTitle(
    text: String
) {
    Text(
        text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun BackButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.topbar_back_content_description)
        )
    }
}

@Composable
fun DoneButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = stringResource(R.string.topbar_done_content_description)
        )
    }
}

@Composable
fun DeleteButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.topbar_delete_content_description)
        )
    }
}

@Composable
fun CloseButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.topbar_close_content_description)
        )
    }
}
