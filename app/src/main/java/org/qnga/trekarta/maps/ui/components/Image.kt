package org.qnga.trekarta.maps.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import org.qnga.trekarta.maps.ui.util.decodeAssetBitmap

@Composable
fun ImageAsset(
    modifier: Modifier = Modifier,
    filename: String,
    contentDescription: String,

    ) {
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bitmapState = context.decodeAssetBitmap(filename).getOrNull()
    }

    if (null != bitmapState) {
        val bitmap = bitmapState!!.asImageBitmap()
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}
