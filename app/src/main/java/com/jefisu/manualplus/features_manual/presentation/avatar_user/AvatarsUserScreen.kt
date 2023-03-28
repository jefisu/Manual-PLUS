package com.jefisu.manualplus.features_manual.presentation.avatar_user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.AvatarImage
import com.jefisu.manualplus.core.presentation.components.BottomSheet
import com.jefisu.manualplus.core.presentation.ui.theme.light_Primary
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.features_manual.presentation.SharedState
import com.jefisu.manualplus.features_manual.presentation.avatar_user.components.ShimmerEffectList
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun AvatarsUserScreen(
    sharedState: SharedState,
    navController: NavController,
    viewModel: AvatarsUserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    LaunchedEffect(key1 = Unit) {
        viewModel.selectAvatar(sharedState.avatarUri)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AvatarsUserViewModel.UiEvent.Navigate -> {
                    navController.navigateUp()
                }
                is AvatarsUserViewModel.UiEvent.ShowError -> {
                    sheetState.show()
                }
            }
        }
    }

    BottomSheet(
        error = state.error?.asString().orEmpty(),
        sheetState = sheetState,
        onOkClick = viewModel::errorDisplayed
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.small)
                .padding(top = 12.dp)
        ) {
            AvatarImage(
                image = state.avatar,
                clickEnabled = false,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = navController::navigateUp,
                    modifier = Modifier.offset(x = (-4).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = null
                    )
                }
                Text(
                    text = sharedState.user?.name ?: stringResource(R.string.user),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.save).uppercase(),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .clickable(
                            enabled = sharedState.avatarUri != state.avatar,
                            onClick = viewModel::updateAvatarUser
                        )
                        .padding(MaterialTheme.spacing.small)
                )
            }
            Divider()
            ShimmerEffectList(
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    userScrollEnabled = !state.isLoading
                ) {
                    itemsIndexed(state.availableAvatars) { i, uri ->
                        val size = state.availableAvatars.size
                        val threeLastUri =
                            (size - state.availableAvatars.calculateNumberFromLastItems()) until size
                        Box(
                            modifier = Modifier.padding(
                                top = if (i in 0..2) MaterialTheme.spacing.medium else MaterialTheme.spacing.default,
                                bottom = if (i in threeLastUri) MaterialTheme.spacing.medium else MaterialTheme.spacing.default,
                            )
                        ) {
                            AvatarImage(
                                image = uri,
                                size = 100.dp,
                                offsetY = 14.dp,
                                iconAction = if (state.avatar == uri) R.drawable.ic_check_circle else null,
                                shapeIcon = CircleShape,
                                iconColor = light_Primary,
                                iconBackground = MaterialTheme.colors.background,
                                onClick = { viewModel.selectAvatar(uri) },
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun List<Any>.calculateNumberFromLastItems(): Int {
    val remainder = size % 3
    return if (remainder == 0) 3 else remainder
}