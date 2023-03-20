package com.jefisu.manualplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.jefisu.manualplus.core.data.MongoClient
import com.jefisu.manualplus.core.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.features_manual.presentation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MongoClient.configureRealm()
        setContent {
            ManualPLUSTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}