package org.qnga.trekarta.maps.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.qnga.trekarta.maps.MainApplication
import org.qnga.trekarta.maps.ui.theme.TrekartaAdditionalMaps

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        val app = application as MainApplication
        MainViewModel.Factory(app.mapRepository) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) {
            val shouldFinish = viewModel.onBackstackPressed()
            if (shouldFinish) {
                finish()
            }
        }

        setContent {
            TrekartaAdditionalMaps {
                val screen by viewModel.currentScreen.collectAsState()
                screen.Screen()
            }
        }
    }
}
