package com.jefisu.manualplus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import com.jefisu.manualplus.core.data.MongoClient
import com.jefisu.manualplus.core.data.database.FileToUploadDao
import com.jefisu.manualplus.core.presentation.SharedViewModel
import com.jefisu.manualplus.core.presentation.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.util.retryUploadFile
import com.jefisu.manualplus.destinations.ProfileUserScreenDestination
import com.jefisu.manualplus.features_user.presentation.profile_user.ProfileUserScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var fileToUploadDao: FileToUploadDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cleanupCheck()
        MongoClient.configureRealm()
        setContent {
            val sharedViewModel = hiltViewModel<SharedViewModel>()

            ManualPLUSTheme {
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