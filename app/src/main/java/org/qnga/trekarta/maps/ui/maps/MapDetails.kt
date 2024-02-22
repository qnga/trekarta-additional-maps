package org.qnga.trekarta.maps.ui.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.qnga.trekarta.maps.core.maps.BritainOsOutdoorProvider
import org.qnga.trekarta.maps.core.maps.BritainOsRoadProvider
import org.qnga.trekarta.maps.core.maps.FranceIgnScan25Provider
import org.qnga.trekarta.maps.core.maps.SpainIgnMtnProvider
import org.qnga.trekarta.maps.ui.components.ImageAsset

@Composable
fun MapDetailsForm(
    settingsEditor: MapSettingsEditor
) {
    when (settingsEditor) {
        is SpainIgnMtnSettingsEditor ->
            SpainIgnMtnDetailsForm()
        is FranceIgnScan25SettingsEditor ->
            FranceIgnScan25DetailsForm(settingsEditor)
        is BritainOsRoadSettingsEditor ->
            BritainOsRoadDetailsForm(settingsEditor)
        is BritainOsOutdoorSettingsEditor ->
            BritainOsOutdoorDetailsForm(settingsEditor)
        is CustomWmtsKvpSettingsEditor ->
            CustomWmtsKvpDetailsForm(settingsEditor)
    }
}

@Composable
private fun MapDetailsFormScaffold(
    title: String,
    sampleName: String? = null,
    rest: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            sampleName?.let {
                ImageAsset(
                    modifier = Modifier.size(200.dp),
                    fileName = it,
                    contentDescription = ""
                )
            }

            rest()
        }
    }
}

@Composable
private fun SpainIgnMtnDetailsForm() {
    MapDetailsFormScaffold(
        title = SpainIgnMtnProvider.title,
        sampleName = "SpainIgnMtnTilematrix=14&tilerow=6177&tilecol=8107.jpg"
    ) {
        val legend = buildAnnotatedString {
            append("Leyenda")
        }

        val uriHandler = LocalUriHandler.current

        ClickableText(
            text = legend,
            style = linkStyle,
            onClick = {
                uriHandler.openUri("https://www.ign.es/wmts/mapa-raster/leyendas/mtn25.png")
            }
        )
    }
}

@Composable
private fun FranceIgnScan25DetailsForm(
    settingsEditor: FranceIgnScan25SettingsEditor
) {
    MapDetailsFormScaffold(
        title =  FranceIgnScan25Provider.title,
        sampleName = "FranceIgnScan25Tilematrix=13&tilerow=2914&tilecol=4130.jpg"
    ) {
        val annotatedText = buildAnnotatedString {
            append("L'IGN fournit à ce jour (février 2024) une ")
            pushStringAnnotation(
                tag = "link",
                annotation = "https://geoservices.ign.fr/actualites/2023-11-20-acces-donnesnonlibres-gpf"
            )
            pushStyle(linkStyle.toSpanStyle())
            append("clé ")
            pop()
            pop()
            append(
                "partagée temporaire pour un usage professionnel ou associatif. Plus tard, vous devriez pouvoir " +
                 "en obtenir une personnelle sur un service Web du gouvernement."
            )
        }

        val uriHandler = LocalUriHandler.current

        ClickableText(
            text = annotatedText,
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Justify),
            onClick = {
               val url = annotatedText
                   .getStringAnnotations("link", it, it + 1)
                   .first()

                uriHandler.openUri(url.item)
            }
        )

        TokenForm(
            modifier = Modifier.padding(5.dp),
            label = "Clé de service",
            token = settingsEditor.token
        )
    }
}

@Composable
private fun BritainOsOutdoorDetailsForm(
    settingsEditor: BritainOsOutdoorSettingsEditor
) {
    MapDetailsFormScaffold(
        title =  BritainOsOutdoorProvider.title
    ) {
        TokenForm(
            modifier = Modifier.padding(5.dp),
            label = "API key",
            token = settingsEditor.token
        )
    }
}

@Composable
private fun BritainOsRoadDetailsForm(
    settingsEditor: BritainOsRoadSettingsEditor
) {
    MapDetailsFormScaffold(
        title =  BritainOsRoadProvider.title
    ) {
        TokenForm(
            modifier = Modifier.padding(5.dp),
            label = "API key",
            token = settingsEditor.token
        )
    }
}

@Composable
private fun TokenForm(
    modifier: Modifier,
    label: String,
    token: MutableState<String>
) {
    TokenForm(
        modifier = modifier.fillMaxWidth(),
        label = label,
        token = token.value,
        onTokenChanged = { token.value = it }
    )
}

@Composable
private fun TokenForm(
    modifier: Modifier,
    label: String,
    token: String,
    onTokenChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val showErrors = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.onFocusChanged { if (it.isFocused) showErrors.value = true },
        label = { Text(label) },
        value = token,
        onValueChange = {
            onTokenChanged(it)
        },
        singleLine = true,
        isError = showErrors.value && token.isBlank(),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

private val linkStyle = TextStyle.Default.copy(
    color = Color.Blue,
    textDecoration = TextDecoration.Underline
)
