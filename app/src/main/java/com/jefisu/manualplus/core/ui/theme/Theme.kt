package com.jefisu.manualplus.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = dark_Primary,
    onPrimary = dark_onPrimary,
    secondary = dark_Secondary,
    onSecondary = dark_onSecondary,
    error = dark_Error,
    onError = dark_onError,
    background = on_background,
    onBackground = Background,
    surface = on_background,
    onSurface = Background
)

private val LightColorPalette = lightColors(
    primary = light_Primary,
    onPrimary = Color.White,
    secondary = light_Secondary,
    onSecondary = Color.White,
    error = light_Error,
    onError = Color.White,
    background = Background,
    onBackground = on_background,
    surface = Background,
    onSurface = on_background
)

@Composable
fun ManualPLUSTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}