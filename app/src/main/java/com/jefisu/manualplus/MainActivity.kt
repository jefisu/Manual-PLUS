package com.jefisu.manualplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jefisu.manualplus.core.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.destinations.AuthScreenDestination
import com.jefisu.manualplus.destinations.DirectionDestination
import com.jefisu.manualplus.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ManualPLUSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        startRoute = getStartRoute()
                    )
                }
            }
        }
    }

    private fun getStartRoute(): DirectionDestination {
        if (app.currentUser == null) {
            return AuthScreenDestination
        }
        return HomeScreenDestination
    }
}