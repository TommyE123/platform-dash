package com.platformdash.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GbrNavy = Color(0xFF071D49)
private val GbrRed = Color(0xFFE60000)
private val GbrYellow = Color(0xFFFFD100)
private val GbrWhite = Color(0xFFFFFFFF)

private val LightGbrScheme = lightColorScheme(
    primary = GbrNavy,
    onPrimary = GbrWhite,
    secondary = GbrRed,
    onSecondary = GbrWhite,
    background = GbrWhite,
    onBackground = GbrNavy,
    surface = GbrWhite,
    onSurface = GbrNavy,
    outline = GbrYellow,
)

private val DarkGbrScheme = darkColorScheme(
    primary = GbrRed,
    onPrimary = GbrWhite,
    secondary = GbrNavy,
    onSecondary = GbrWhite,
    background = GbrNavy,
    onBackground = GbrWhite,
    surface = GbrNavy,
    onSurface = GbrWhite,
    outline = GbrYellow,
)

@Composable
fun AppTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    val useDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (useDarkTheme) DarkGbrScheme else LightGbrScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
