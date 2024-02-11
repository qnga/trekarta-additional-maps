package org.qnga.trekarta.maps.ui.maps

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import org.qnga.trekarta.maps.core.maps.BritainOsOutdoorProvider
import org.qnga.trekarta.maps.core.maps.BritainOsRoadProvider
import org.qnga.trekarta.maps.core.maps.CustomWmtsKvpSettings
import org.qnga.trekarta.maps.core.maps.FranceIgnScan25Provider
import org.qnga.trekarta.maps.core.maps.MapSettings
import org.qnga.trekarta.maps.core.maps.SpainIgnMtnProvider
import java.util.UUID

sealed interface MapSettingsEditor {

    val initialSettings: MapSettings?

    val currentSettings: State<MapSettings?>

    fun reset()
}

class FranceIgnScan25SettingsEditor(
    override val initialSettings: FranceIgnScan25Provider.Settings? = null
) : MapSettingsEditor {

    val token: MutableState<String> =
        mutableStateOf(initialSettings?.accessToken.orEmpty())

    override val currentSettings: State<MapSettings?> =
        derivedStateOf {
            token.value
                .takeUnless { it.isBlank() }
                ?.let { FranceIgnScan25Provider.Settings(it) }
        }

    override fun reset() {
        token.value = initialSettings?.accessToken.orEmpty()
    }
}

class SpainIgnMtnSettingsEditor(
    override val initialSettings: MapSettings? = null
) : MapSettingsEditor {

    private val mutableSettings: MutableState<MapSettings> =
        mutableStateOf(SpainIgnMtnProvider.Settings)

    override val currentSettings: State<MapSettings?> =
       mutableSettings

    override fun reset() {
        mutableSettings.value = SpainIgnMtnProvider.Settings
    }
}

class BritainOsOutdoorSettingsEditor(
    override val initialSettings: BritainOsOutdoorProvider.Settings? = null
) : MapSettingsEditor {

    val token: MutableState<String> = mutableStateOf(initialSettings?.accessToken.orEmpty())

    override val currentSettings: State<MapSettings?> =
        derivedStateOf {
            token.value
                .takeUnless { it.isBlank() }
                ?.let { BritainOsOutdoorProvider.Settings(it) }
        }

    override fun reset() {
        token.value = initialSettings?.accessToken.orEmpty()
    }
}

class BritainOsRoadSettingsEditor(
    override val initialSettings: BritainOsRoadProvider.Settings? = null
) : MapSettingsEditor {

    val token: MutableState<String> = mutableStateOf(initialSettings?.accessToken.orEmpty())

    override val currentSettings: State<MapSettings?> =
        derivedStateOf {
            token.value
                .takeUnless { it.isBlank() }
                ?.let { BritainOsRoadProvider.Settings(it) }
        }

    override fun reset() {
        token.value = initialSettings?.accessToken.orEmpty()
    }
}

class CustomWmtsKvpSettingsEditor(
    override val initialSettings: CustomWmtsKvpSettings? = null
) : MapSettingsEditor {

    val id: String =
        initialSettings?.id ?: UUID.randomUUID().toString()

    val title: MutableState<String> =
        mutableStateOf(initialSettings?.title.orEmpty())

    val provider: MutableState<String> =
        mutableStateOf(initialSettings?.provider.orEmpty())

    val minZoom: MutableState<String> =
        mutableStateOf(initialSettings?.minZoom?.toString().orEmpty())

    val maxZoom: MutableState<String> =
        mutableStateOf(initialSettings?.maxZoom?.toString().orEmpty())

    val serviceUrl: MutableState<String> =
        mutableStateOf(initialSettings?.serviceUrl.orEmpty())

    val matrixSet: MutableState<String> =
        mutableStateOf(initialSettings?.matrixSet.orEmpty())

    val layer: MutableState<String> =
        mutableStateOf(initialSettings?.layer.orEmpty())

    val format: MutableState<String> =
        mutableStateOf(initialSettings?.format.orEmpty())

    val style: MutableState<String> =
        mutableStateOf(initialSettings?.style.orEmpty())

    val otherParams: MutableState<List<Pair<String, String>>> =
        mutableStateOf(initialSettings?.otherParams.orEmpty())

    override val currentSettings: State<MapSettings?> =
        derivedStateOf {
            val title = title.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val provider = provider.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val minZoom = minZoom.value
                .toIntOrNull()
                ?: return@derivedStateOf null

            val maxZoom = maxZoom.value
                .toIntOrNull()
                ?: return@derivedStateOf null

            val serviceUrl = serviceUrl.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val matrixSet = matrixSet.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val layer = layer.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val format = format.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val style = style.value
                .takeUnless { it.isBlank() }
                ?: return@derivedStateOf null

            val otherParams = otherParams.value
                .takeIf { it.all { item -> item.first.isNotBlank() && item.second.isNotBlank() } }
                ?: return@derivedStateOf null

            CustomWmtsKvpSettings(
                id = id,
                title = title,
                provider = provider,
                minZoom = minZoom,
                maxZoom = maxZoom,
                serviceUrl = serviceUrl,
                matrixSet = matrixSet,
                layer = layer,
                format = format,
                style = style,
                otherParams = otherParams
            )
        }

    override fun reset() {
        title.value = initialSettings?.title.orEmpty()
        provider.value = initialSettings?.provider.orEmpty()
        minZoom.value = initialSettings?.minZoom?.toString().orEmpty()
        maxZoom.value = initialSettings?.maxZoom?.toString().orEmpty()
        serviceUrl.value = initialSettings?.serviceUrl.orEmpty()
        matrixSet.value = initialSettings?.matrixSet.orEmpty()
        layer.value = initialSettings?.layer.orEmpty()
        format.value = initialSettings?.format.orEmpty()
        style.value = initialSettings?.style.orEmpty()
        otherParams.value = initialSettings?.otherParams.orEmpty()
    }
}
