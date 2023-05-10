package com.jefisu.manualplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.storage.FirebaseStorage
import com.jefisu.manualplus.core.data.FileToUploadDao
import com.jefisu.manualplus.core.presentation.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.util.retryUploadFile
import com.jefisu.manualplus.destinations.AuthScreenDestination
import com.jefisu.manualplus.destinations.AvatarsUserScreenDestination
import com.jefisu.manualplus.destinations.DetailScreenDestination
import com.jefisu.manualplus.destinations.DirectionDestination
import com.jefisu.manualplus.destinations.HomeScreenDestination
import com.jefisu.manualplus.destinations.ProfileUserScreenDestination
import com.jefisu.manualplus.features_auth.presentation.AuthScreen
import com.jefisu.manualplus.features_auth.presentation.AuthViewModel
import com.jefisu.manualplus.features_manual.presentation.avatar_user.AvatarsUserScreen
import com.jefisu.manualplus.features_manual.presentation.avatar_user.AvatarsUserViewModel
import com.jefisu.manualplus.features_manual.presentation.detail.DetailScreen
import com.jefisu.manualplus.features_manual.presentation.detail.DetailViewModel
import com.jefisu.manualplus.features_manual.presentation.home.HomeScreen
import com.jefisu.manualplus.features_manual.presentation.home.HomeViewModel
import com.jefisu.manualplus.features_manual.presentation.profile_user.ProfileUserEvent
import com.jefisu.manualplus.features_manual.presentation.profile_user.ProfileUserScreen
import com.jefisu.manualplus.features_manual.presentation.profile_user.ProfileUserViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val mainViewModel = hiltViewModel<MainViewModel>()
            val user by mainViewModel.user.collectAsStateWithLifecycle()
            val avatarPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(applicationContext)
                    .data(user.second)
                    .crossfade(true)
                    .decoderDispatcher(Dispatchers.IO)
                    .build()
            )

            ManualPLUSTheme {
                Surface(color = MaterialTheme.colors.background) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        startRoute = getStartScreen(),
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .statusBarsPadding()
                    ) {
                        composable(AuthScreenDestination) {
                            val viewModel = hiltViewModel<AuthViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            AuthScreen(
                                navController = navController,
                                state = state,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable(HomeScreenDestination) {
                            val viewModel = hiltViewModel<HomeViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(Unit) {
                                mainViewModel.loadUser()
                            }

                            HomeScreen(
                                pairUser = user,
                                state = state,
                                onDataLoaded = { isLoading = false },
                                navigateToDetail = { id, imageUrl ->
                                    navController.navigate(DetailScreenDestination(id, imageUrl))
                                },
                                navigateToProfile = {
                                    navController.navigate(ProfileUserScreenDestination)
                                }
                            )
                        }
                        composable(DetailScreenDestination) {
                            val viewModel = hiltViewModel<DetailViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            DetailScreen(
                                state = state,
                                onClickGoBackHome = navController::navigateUp
                            )
                        }
                        composable(ProfileUserScreenDestination) {
                            val viewModel = hiltViewModel<ProfileUserViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            ProfileUserScreen(
                                user = user.first,
                                avatarPainter = avatarPainter,
                                state = state,
                                onEvent = viewModel::onEvent,
                                goBackToHome = navController::navigateUp,
                                navigateToAvatarsUser = {
                                    navController.navigate(AvatarsUserScreenDestination(user.second))
                                },
                                logoutUser = {
                                    viewModel.onEvent(ProfileUserEvent.Logout)
                                    navController.apply {
                                        backQueue.clear()
                                        navigate(AuthScreenDestination)
                                    }
                                }
                            )
                        }
                        composable(AvatarsUserScreenDestination) {
                            val viewModel = hiltViewModel<AvatarsUserViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            AvatarsUserScreen(
                                avatarUserUrl = user.second.orEmpty(),
                                user = user.first,
                                state = state,
                                goBackToProfile = navController::navigateUp,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getStartScreen(): DirectionDestination {
        return if (app.currentUser != null) {
            isLoading = true
            HomeScreenDestination
        } else AuthScreenDestination
    }

    private fun cleanupCheck() {
        val storage = FirebaseStorage.getInstance()
        lifecycleScope.launch(Dispatchers.IO) {
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