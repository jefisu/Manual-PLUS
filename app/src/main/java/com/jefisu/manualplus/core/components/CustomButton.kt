package com.jefisu.manualplus.core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.ui.theme.spacing

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    buttonShape: Shape = CircleShape,
    backgroundIsPrimary: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = buttonShape,
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (backgroundIsPrimary) MaterialTheme.colors.background else MaterialTheme.colors.primary,
            contentColor = if (backgroundIsPrimary) MaterialTheme.colors.primary else MaterialTheme.colors.background
        )
    ) {
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
            fontWeight = FontWeight.Bold
        )
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

@Preview
@Composable
fun PreviewGoogleButton() {
    CustomButton(
        text = "Sign In with Google",
        icon = R.drawable.google_logo,
        onClick = { }
    )
}