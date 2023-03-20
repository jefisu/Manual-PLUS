package com.jefisu.manualplus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.manualplus.core.data.MongoClient
import com.jefisu.manualplus.core.presentation.SharedViewModel
import com.jefisu.manualplus.core.presentation.ThemeViewModel
import com.jefisu.manualplus.core.presentation.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.util.Theme
import com.jefisu.manualplus.destinations.ProfileUserScreenDestination
import com.jefisu.manualplus.features_user.presentation.profile_user.ProfileUserScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MongoClient.configureRealm()
        setContent {
            val sharedViewModel = hiltViewModel<SharedViewModel>()
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val theme by themeViewModel.theme.collectAsState()

            ManualPLUSTheme(
                darkTheme = when (theme) {
                    Theme.Dark -> true
                    Theme.Light -> false
                    else -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(sharedViewModel)
                        },
                        manualComposableCallsBuilder = {
                            composable(ProfileUserScreenDestination) {
                                ProfileUserScreen(
                                    navigator = destinationsNavigator,
                                    sharedViewModel = sharedViewModel,
                                    themeViewModel = themeViewModel,
                                    logout = {
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                AuthActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}