package org.qnga.trekarta.maps.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.decodeAssetBitmap(filename: String): Result<Bitmap> =
    try {
        withContext(Dispatchers.IO) {
            val stream = assets.open(filename)
            BitmapFactory.decodeStream(stream)
                ?.let { Result.success(it) }
                ?: Result.failure(Exception("Could not decode asset bitmap."))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
