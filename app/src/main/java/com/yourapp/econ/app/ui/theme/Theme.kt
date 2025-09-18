package com.yourapp.econ.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = neutral700,
    onPrimary = neutral100,
    primaryContainer = neutral800,
    onPrimaryContainer = neutral100,
    inversePrimary = neutral200,

    secondary = neutral200,
    onSecondary = neutral800,
    secondaryContainer = neutral300,
    onSecondaryContainer = neutral900,

    tertiary = neutral500,
    onTertiary = neutral900,
    tertiaryContainer = neutral100,
    onTertiaryContainer = neutral800,

    // background is the main background color for the app
    background = neutral50,
    onBackground = neutral600,

    // surface is the main background color for most components
    surface = neutral100,
    onSurface = neutral700,

    // surfaceVariant is used for components like chips, buttons, etc.
    surfaceVariant = neutral200,
    onSurfaceVariant = neutral700,
    surfaceTint = neutral700,

    //Snackbar
    inverseSurface = neutral900,
    inverseOnSurface = neutral100,

    error = Color(0xFFC94F4F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFE5E5),
    onErrorContainer = Color(0xFF400000),

    // outline colors
    outline = neutral600,
    outlineVariant = neutral300,

    // shadow
    scrim = Color.Black.copy(alpha = 0.5f),

    surfaceBright = neutral100,
    surfaceDim = neutral200,

    // bottom sheets and dialogs
    surfaceContainer = neutral100,
    surfaceContainerLow = neutral200,
    surfaceContainerLowest = neutral50,
    surfaceContainerHigh = neutral300,

    //background cards
    surfaceContainerHighest = neutral50
)



val DarkColorScheme = darkColorScheme(
    primary = neutral100,
    onPrimary = neutral900,
    primaryContainer = neutral200,
    onPrimaryContainer = neutral800,
    inversePrimary = neutral700,

    secondary = neutral800,
    onSecondary = neutral200,
    secondaryContainer = neutral700,
    onSecondaryContainer = neutral100,

    tertiary = neutral300,
    onTertiary = neutral100,
    tertiaryContainer = neutral800,
    onTertiaryContainer = neutral200,

    background = neutral950,
    onBackground = neutral200,

    surface = neutral900,
    onSurface = neutral100,

    surfaceVariant = neutral800,
    onSurfaceVariant = neutral200,
    surfaceTint = neutral100,

    inverseSurface = neutral100,
    inverseOnSurface = neutral900,

    error = Color(0xFFE57373),
    onError = Color(0xFF1C1C1C),
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = Color(0xFFFFE5E5),

    outline = neutral400,
    outlineVariant = neutral500,
    scrim = Color.Black.copy(alpha = 0.7f),

    surfaceBright = neutral800,
    surfaceDim = neutral900,

    surfaceContainer = neutral800,
    surfaceContainerLow = neutral900,
    surfaceContainerLowest = neutral950,
    surfaceContainerHigh = neutral700,
    surfaceContainerHighest = neutral600
)


@Composable
fun EconTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}