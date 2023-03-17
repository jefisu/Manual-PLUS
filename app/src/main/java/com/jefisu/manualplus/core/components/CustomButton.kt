package com.jefisu.manualplus.core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.core.ui.theme.spacing

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    buttonShape: Shape = CircleShape,
    backgroundIsPrimary: Boolean = false,
    isLoading: Boolean = false
) {
    val backgroundColor =
        if (backgroundIsPrimary) MaterialTheme.colors.background else MaterialTheme.colors.primary
    val contentColor =
        if (backgroundIsPrimary) MaterialTheme.colors.primary else MaterialTheme.colors.background

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(buttonShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
            .background(backgroundColor)
            .then(modifier)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor
            )
        } else {
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            }
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Preview
@Composable
fun PreviewCustomButton() {
    CustomButton(
        text = "Log In",
        onClick = { }
    )
}