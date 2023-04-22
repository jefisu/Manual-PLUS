package com.jefisu.manualplus.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.core.presentation.ui.theme.light_Primary

@Composable
fun AvatarImage(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    clickEnabled: Boolean = true,
    @DrawableRes iconAction: Int? = null,
    shapeIcon: Shape = RoundedCornerShape(8.dp),
    iconColor: Color = MaterialTheme.colors.background,
    iconBackground: Color = MaterialTheme.colors.onBackground,
    size: Dp = 50.dp,
    shape: Shape = CircleShape,
    background: Color = light_Primary,
    isMirrored: Boolean = false,
    offsetY: Dp = 6.dp
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .background(background)
                .clickable(
                    enabled = clickEnabled,
                    onClick = onClick
                )
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .offset(y = offsetY)
                    .scale(
                        scaleX = if (isMirrored) -1f else 1f,
                        scaleY = 1f
                    )
            )
        }
        if (iconAction != null) {
            Icon(
                painter = painterResource(iconAction),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(size * 0.30f)
                    .align(Alignment.BottomEnd)
                    .clip(shapeIcon)
                    .background(iconBackground)
            )
        }
    }
}