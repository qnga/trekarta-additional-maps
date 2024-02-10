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

    val token: MutableState<String?> =
        mutableStateOf(initialSettings?.accessToken)

    override val currentSettings: State<MapSettings?> =
        derivedStateOf { token.value?.let { FranceIgnScan25Provider.Settings(it) } }

    override fun reset() {
        token.value = initialSettings?.accessToken
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

    val token: MutableState<String?> = mutableStateOf(initialSettings?.accessToken)

    override val currentSettings: State<MapSettings?> =
        derivedStateOf { token.value?.let { BritainOsOutdoorProvider.Settings(it) } }

    override fun reset() {
        token.value = initialSettings?.accessToken
    }
}

class BritainOsRoadSettingsEditor(
    override val initialSettings: BritainOsRoadProvider.Settings? = null
) : MapSettingsEditor {

    val token: MutableState<String?> = mutableStateOf(initialSettings?.accessToken)

    override val currentSettings: State<MapSettings?> =
        derivedStateOf { token.value?.let { BritainOsRoadProvider.Settings(it) } }

    override fun reset() {
        token.value = initialSettings?.accessToken
    }
}

class CustomWmtsKvpSettingsEditor(
    override val initialSettings: CustomWmtsKvpSettings? = null
) : MapSettingsEditor {

    private val id: String = initialSettings?.id ?: UUID.randomUUID().toString()
    val title: MutableState<String?> = mutableStateOf(initialSettings?.title)
    val provider: MutableState<String?> = mutableStateOf(initialSettings?.provider)
    val minZoom: MutableState<Int?> = mutableStateOf(initialSettings?.minZoom)
    val maxZoom: MutableState<Int?> = mutableStateOf(initialSettings?.maxZoom)
    val serviceUrl: MutableState<String?> = mutableStateOf(initialSettings?.serviceUrl)
    val matrixSet: MutableState<String?> = mutableStateOf(initialSettings?.matrixSet)
    val layer: MutableState<String?> = mutableStateOf(initialSettings?.layer)
    val format: MutableState<String?> = mutableStateOf(initialSettings?.format)
    val style: MutableState<String?> = mutableStateOf(initialSettings?.style)
    val otherParams: MutableState<Map<String, String>> = mutableStateOf(initialSettings?.otherParams.orEmpty().toMap())

    override val currentSettings: State<MapSettings?> =
        derivedStateOf {
            val title = title.value ?: return@derivedStateOf null
            val provider = provider.value ?: return@derivedStateOf null
            val minZoom = minZoom.value ?: return@derivedStateOf null
            val maxZoom = maxZoom.value ?: return@derivedStateOf null
            val serviceUrl = serviceUrl.value ?: return@derivedStateOf null
            val matrixSet = matrixSet.value ?: return@derivedStateOf null
            val layer = layer.value ?: return@derivedStateOf null
            val format = format.value ?: return@derivedStateOf null
            val style = style.value ?: return@derivedStateOf null
            val otherParams = otherParams.value.toList()

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
        title.value = initialSettings?.title
        provider.value = initialSettings?.provider
        minZoom.value = initialSettings?.minZoom
        maxZoom.value = initialSettings?.maxZoom
        serviceUrl.value = initialSettings?.serviceUrl
        matrixSet.value = initialSettings?.matrixSet
        layer.value = initialSettings?.layer
        format.value = initialSettings?.format
        style.value = initialSettings?.style
        otherParams.value = initialSettings?.otherParams.orEmpty().toMap()
    }
}
