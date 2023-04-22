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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.AvatarImage
import com.jefisu.manualplus.core.presentation.components.BottomSheet
import com.jefisu.manualplus.core.presentation.ui.theme.light_Primary
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.features_manual.domain.model.User
import com.jefisu.manualplus.features_manual.presentation.avatar_user.components.ShimmerEffectList
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun AvatarsUserScreen(
    user: User?,
    avatarUserUrl: String,
    state: AvatarsUserState,
    goBackToProfile: () -> Unit,
    onEvent: (AvatarsUserEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val avatarPainter = rememberAsyncImagePainter(model = state.avatar)

    LaunchedEffect(key1 = state.uiEvent) {
        state.uiEvent?.let { event ->
            when (event) {
                is AvatarsUserViewModel.UiEvent.Navigate -> {
                    goBackToProfile()
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
        onOkClick = { onEvent(AvatarsUserEvent.ErrorDisplayed) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.small)
                .padding(top = 12.dp)
        ) {
            AvatarImage(
                painter = avatarPainter,
                clickEnabled = false,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = goBackToProfile,
                    modifier = Modifier.offset(x = (-4).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = null
                    )
                }
                Text(
                    text = user?.name.orEmpty(),
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
                        .clickable {
                            if (avatarUserUrl == state.avatar) {
                                goBackToProfile()
                                return@clickable
                            }
                            onEvent(AvatarsUserEvent.SaveUserAvatar)
                        }
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
                    itemsIndexed(state.availableAvatars) { i, avatarUrl ->
                        val size = state.availableAvatars.size
                        val threeLastImage =
                            (size - state.availableAvatars.calculateNumberFromLastItems()) until size

                        val avatarPainter = rememberAsyncImagePainter(model = avatarUrl)
                        Box(
                            modifier = Modifier.padding(
                                top = if (i in 0..2) MaterialTheme.spacing.medium else MaterialTheme.spacing.default,
                                bottom = if (i in threeLastImage) MaterialTheme.spacing.medium else MaterialTheme.spacing.default,
                            )
                        ) {
                            AvatarImage(
                                painter = avatarPainter,
                                size = 100.dp,
                                offsetY = 14.dp,
                                iconAction = if (state.avatar == avatarUrl) R.drawable.ic_check_circle else null,
                                shapeIcon = CircleShape,
                                iconColor = light_Primary,
                                iconBackground = MaterialTheme.colors.background,
                                onClick = { onEvent(AvatarsUserEvent.EnterAvatar(avatarUrl)) },
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