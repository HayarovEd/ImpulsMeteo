package com.edurda77.impulsmeteo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.edurda77.resources.theme.Typography
import androidx.compose.ui.platform.LocalContext
import com.edurda77.resources.theme.backgroundDark
import com.edurda77.resources.theme.backgroundLight
import com.edurda77.resources.theme.errorContainerDark
import com.edurda77.resources.theme.errorContainerLight
import com.edurda77.resources.theme.errorDark
import com.edurda77.resources.theme.errorLight
import com.edurda77.resources.theme.inverseOnSurfaceDark
import com.edurda77.resources.theme.inverseOnSurfaceLight
import com.edurda77.resources.theme.inversePrimaryDark
import com.edurda77.resources.theme.inversePrimaryLight
import com.edurda77.resources.theme.inverseSurfaceDark
import com.edurda77.resources.theme.inverseSurfaceLight
import com.edurda77.resources.theme.onBackgroundDark
import com.edurda77.resources.theme.onBackgroundLight
import com.edurda77.resources.theme.onErrorContainerDark
import com.edurda77.resources.theme.onErrorContainerLight
import com.edurda77.resources.theme.onErrorDark
import com.edurda77.resources.theme.onErrorLight
import com.edurda77.resources.theme.onPrimaryContainerDark
import com.edurda77.resources.theme.onPrimaryContainerLight
import com.edurda77.resources.theme.onPrimaryDark
import com.edurda77.resources.theme.onPrimaryLight
import com.edurda77.resources.theme.onSecondaryContainerDark
import com.edurda77.resources.theme.onSecondaryContainerLight
import com.edurda77.resources.theme.onSecondaryDark
import com.edurda77.resources.theme.onSecondaryLight
import com.edurda77.resources.theme.onSurfaceDark
import com.edurda77.resources.theme.onSurfaceLight
import com.edurda77.resources.theme.onSurfaceVariantDark
import com.edurda77.resources.theme.onSurfaceVariantLight
import com.edurda77.resources.theme.onTertiaryContainerDark
import com.edurda77.resources.theme.onTertiaryContainerLight
import com.edurda77.resources.theme.onTertiaryDark
import com.edurda77.resources.theme.onTertiaryLight
import com.edurda77.resources.theme.outlineDark
import com.edurda77.resources.theme.outlineLight
import com.edurda77.resources.theme.outlineVariantDark
import com.edurda77.resources.theme.outlineVariantLight
import com.edurda77.resources.theme.primaryContainerDark
import com.edurda77.resources.theme.primaryContainerLight
import com.edurda77.resources.theme.primaryDark
import com.edurda77.resources.theme.primaryLight
import com.edurda77.resources.theme.scrimDark
import com.edurda77.resources.theme.scrimLight
import com.edurda77.resources.theme.secondaryContainerDark
import com.edurda77.resources.theme.secondaryContainerLight
import com.edurda77.resources.theme.secondaryDark
import com.edurda77.resources.theme.secondaryLight
import com.edurda77.resources.theme.surfaceBrightDark
import com.edurda77.resources.theme.surfaceBrightLight
import com.edurda77.resources.theme.surfaceContainerDark
import com.edurda77.resources.theme.surfaceContainerHighDark
import com.edurda77.resources.theme.surfaceContainerHighLight
import com.edurda77.resources.theme.surfaceContainerHighestDark
import com.edurda77.resources.theme.surfaceContainerHighestLight
import com.edurda77.resources.theme.surfaceContainerLight
import com.edurda77.resources.theme.surfaceContainerLowDark
import com.edurda77.resources.theme.surfaceContainerLowLight
import com.edurda77.resources.theme.surfaceContainerLowestDark
import com.edurda77.resources.theme.surfaceContainerLowestLight
import com.edurda77.resources.theme.surfaceDark
import com.edurda77.resources.theme.surfaceDimDark
import com.edurda77.resources.theme.surfaceDimLight
import com.edurda77.resources.theme.surfaceLight
import com.edurda77.resources.theme.surfaceVariantDark
import com.edurda77.resources.theme.surfaceVariantLight
import com.edurda77.resources.theme.tertiaryContainerDark
import com.edurda77.resources.theme.tertiaryContainerLight
import com.edurda77.resources.theme.tertiaryDark
import com.edurda77.resources.theme.tertiaryLight

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun ImpulsMeteoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}