package com.jefisu.manualplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import com.jefisu.manualplus.core.data.FileToUploadDao
import com.jefisu.manualplus.core.presentation.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.util.retryUploadFile
import com.jefisu.manualplus.destinations.AuthScreenDestination
import com.jefisu.manualplus.destinations.DirectionDestination
import com.jefisu.manualplus.destinations.HomeScreenDestination
import com.jefisu.manualplus.destinations.ProfileUserScreenDestination
import com.jefisu.manualplus.features_manual.presentation.SharedViewModel
import com.jefisu.manualplus.features_manual.presentation.home.HomeScreen
import com.jefisu.manualplus.features_manual.presentation.profile_user.ProfileUserScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var fileToUploadDao: FileToUploadDao

    @Inject
    lateinit var app: App

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            isLoading
        }
        cleanupCheck()
        setContent {
            val sharedViewModel = hiltViewModel<SharedViewModel>()
            val state by sharedViewModel.state.collectAsStateWithLifecycle()

            ManualPLUSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        startRoute = getStartDestination(),
                        dependenciesContainerBuilder = {
                            dependency(state)
                        },
                        manualComposableCallsBuilder = {
                            composable(HomeScreenDestination) {
                                HomeScreen(
                                    sharedState = state,
                                    navigator = destinationsNavigator,
                                    loadUserData = sharedViewModel::getUser,
                                    onDataLoaded = { isLoading = false }
                                )
                            }
                            composable(ProfileUserScreenDestination) {
                                ProfileUserScreen(
                                    navController = navController,
                                    sharedState = state
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun getStartDestination(): DirectionDestination {
        return if (app.currentUser != null) {
            isLoading = true
            HomeScreenDestination
        } else AuthScreenDestination
    }

    private fun cleanupCheck() {
        val storage = FirebaseStorage.getInstance()
        lifecycleScope.launch {
            fileToUploadDao
                .getAllFileToUpload()
                .forEach { imageFile ->
                    storage.retryUploadFile(
                        fileBytes = imageFile.fileBytes,
                        remotePath = imageFile.remotePath,
                        sessionUri = imageFile.sessionUri.toUri(),
                        onSuccess = {
                            launch { fileToUploadDao::deleteFileToUpload }
                        }
                    )
                }
        }
    }
}