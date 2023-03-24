package com.jefisu.manualplus.features_manual.presentation.avatar_user.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.core.util.shimmerEffect

@Composable
fun ShimmerEffectList(
    isLoading: Boolean = false,
    contentAfterLoading: @Composable () -> Unit,
) {
    Box {
        AnimatedVisibility(
            visible = isLoading,
            exit = fadeOut(animationSpec = tween(700))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                userScrollEnabled = false
            ) {
                items(15) {
                    Box(
                        modifier = Modifier
                            .padding(
                                top = if (it in 0..2) MaterialTheme.spacing.medium
                                else MaterialTheme.spacing.default
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                                .shimmerEffect()
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(animationSpec = tween(700))
        ) {
            contentAfterLoading()
        }
    }
}