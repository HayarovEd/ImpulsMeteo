package com.edurda77.resources.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val light_color_primary = Color(0xFF0063cc)
val light_color_primary_hover = Color(0xFF0069d9)
val light_color_success = Color(0xFF00ac47)
val light_color_success_hover = Color(0xFF09b651)
val light_color_danger = Color(0xFFf52222)
val light_color_danger_hover = Color(0xFFe63b63)
val light_color_warning = Color(0xFFffc107)
val light_color_background_app = Color(0xFFf7f7f7)
val light_color_background_primary = Color(0xFFedf4fb)
val light_color_background_primary_hover = Color(0xFFe3eefa)
val light_color_background_success = Color(0xFFecfbf2)
val light_color_background_danger = Color(0xFFfee0e0)
val light_color_background_danger_hover = Color(0xFFfdcaca)
val light_color_background = Color(0xFFffffff)
val light_color_background_item = Color(0xFFf5f5f5)
val light_color_background_item_hover = Color(0xFFececec)
val light_color_background_muted = Color(0xFFdfdfdf)
val light_color_background_title = Color(0xFF6d6d6d)
val light_color_text = Color(0xFF000000)
val light_color_text_disabled = Color(0xFF858587)
val light_color_text_disabled_hover = Color(0xFF5e5e5e)
val light_color_text_accent = Color(0xFFffffff)


val dark_color_primary = Color(0xFF0189cd)
val dark_color_primary_hover = Color(0xFF1a95d2)
val dark_color_success = Color(0xFF00ac47)
val dark_color_success_hover = Color(0xFF09b651)
val dark_color_danger = Color(0xFFf52222)
val dark_color_warning = Color(0xFFffc107)
val dark_color_background_primary = Color(0xFF143c52)
val dark_color_background_primary_hover = Color(0xFF20516c)
val dark_color_background_success = Color(0xFFd7f3e2)
val dark_color_background_danger = Color(0xFF593a3e)
val dark_color_background_danger_hover = Color(0xFF74565a)
val dark_color_background_app = Color(0xFF1e1f22)
val dark_color_background = Color(0xFF313338)
val dark_color_background_item = Color(0xFF393c41)
val dark_color_background_item_hover = Color(0xFF42464b)
val dark_color_background_muted = Color(0xFF474747)
val dark_color_background_title = Color(0xFF111214)
val dark_color_text = Color(0xFFffffff)
val dark_color_text_disabled = Color(0xFF949ba4)
val dark_color_text_disabled_hover = Color(0xFFacb3bd)
val dark_color_text_accent = Color(0xFFffffff)


val lightScheme2 = lightColorScheme(
    primary = light_color_primary,
    onPrimary = light_color_text,
    primaryContainer = light_color_background_item,
    onPrimaryContainer = light_color_text,
    secondary = light_color_background_primary,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = light_color_danger,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = light_color_background_app,
    onBackground = light_color_text,
    surface = light_color_background_app,
    onSurface = light_color_text,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = light_color_text_disabled,
    outlineVariant = light_color_success,
    scrim = scrimLight,
    inverseSurface = light_color_background_muted,
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

val darkScheme2 = darkColorScheme(
    primary = dark_color_primary,
    onPrimary = dark_color_text,
    primaryContainer = dark_color_background_item,
    onPrimaryContainer = dark_color_text,
    secondary = dark_color_background_primary,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = dark_color_danger,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = dark_color_background_app,
    onBackground = dark_color_text,
    surface = dark_color_background_app,
    onSurface = dark_color_text,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = dark_color_text_disabled,
    outlineVariant = dark_color_success,
    scrim = scrimDark,
    inverseSurface = dark_color_background_muted,
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