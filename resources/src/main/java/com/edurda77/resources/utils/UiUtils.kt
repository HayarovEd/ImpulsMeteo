package com.edurda77.resources.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

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
        if (updatedRed > 255) 255 else updatedRed,
        if (updatedGreen > 255) 255 else updatedGreen,
        if (updatedBlue > 255) 255 else updatedBlue
    )
}