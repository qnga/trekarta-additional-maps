package org.qnga.trekarta.maps.ui.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AppTopBar() {
    TopAppBar(
        title = {
            Text(
                "Trekarta Additional Maps",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}
