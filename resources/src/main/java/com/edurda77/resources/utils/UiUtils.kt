package com.edurda77.resources.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.pow

fun hexToBrush(
    hex: String
): Brush {
    val firstColor = hexToColor(
        hex = hex,
        amount = -1
    )
    val secondColor = hexToColor(
        hex = hex,
        amount = 1
    )
    return Brush.verticalGradient(
        listOf(firstColor, secondColor)
    )
}

private fun hexToColor(
    hex: String,
): Color {
    require(hex.length == 7 && hex[0] == '#') { "Invalid hex color format" }
    val red = hex.substring(1, 3).toInt(16)
    val green = hex.substring(3, 5).toInt(16)
    val blue = hex.substring(5, 7).toInt(16)
    return Color(red, green, blue)
}

private fun hexToColor(
    hex: String,
    amount: Int
): Color {
    require(hex.length == 7 && hex[0] == '#') { "Invalid hex color format" }
    val red = hex.substring(1, 3).toInt(16)
    val green = hex.substring(3, 5).toInt(16)
    val blue = hex.substring(5, 7).toInt(16)
    val updatedRed = red + 25 * amount
    val updatedGreen = green + 15 * amount
    val updatedBlue = blue + 10 * amount
    return Color(
        if (updatedRed > 255) 255 else if (updatedRed < 0) 0 else updatedRed,
        if (updatedGreen > 255) 255 else if (updatedGreen < 0) 0 else updatedGreen,
        if (updatedBlue > 255) 255 else if (updatedBlue < 0) 0 else updatedBlue
    )
}

fun getContrastColor(hex: String): Color {
    val luminance = calculateLuminance(hex)
    return if (luminance > 0.5) Color.Black else Color.White
}

fun calculateLuminance(hex: String): Double {
    val color = hexToColor(hex)
    val red = color.red
    val green = color.green
    val blue = color.blue

    val sr = if (red <= 0.03928) red / 12.92 else ((red + 0.055) / 1.055).pow(2.4)
    val sg = if (green <= 0.03928) green / 12.92 else ((green + 0.055) / 1.055).pow(2.4)
    val sb = if (blue <= 0.03928) blue / 12.92 else ((blue + 0.055) / 1.055).pow(2.4)
    return 0.2126 * sr + 0.7152 * sg + 0.0722 * sb
}