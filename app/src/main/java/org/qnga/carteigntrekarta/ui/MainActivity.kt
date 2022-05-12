package org.qnga.carteigntrekarta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.map
import org.qnga.carteigntrekarta.MainApplication
import org.qnga.carteigntrekarta.ui.theme.CarteIgnTrekartaTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = applicationContext as MainApplication

        setContent {
            CarteIgnTrekartaTheme {
                val accessKey = application.key.map { it.orEmpty() }
                KeyScreen(
                    initialKey = accessKey.collectAsState(""),
                    onKeySubmitted = { application.updateKey(it.takeUnless(String::isEmpty)) }
                )
            }
        }
    }
}
