package com.edurda77.resources.uikit

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.edurda77.domain.model.MeasurementUnit
import com.edurda77.resources.R

/*@Composable
fun Int.asUiIconParam(): ImageVector {
    return when (this) {
        1 -> ImageVector.vectorResource(R.drawable.no_icon)
        2 -> ImageVector.vectorResource(R.drawable.low_temperature)
        3 -> ImageVector.vectorResource(R.drawable.humidity_percentage)
        4 -> ImageVector.vectorResource(R.drawable.pressure)
        5 -> ImageVector.vectorResource(R.drawable.sea_height)
        else -> ImageVector.vectorResource(R.drawable.no_icon)
    }
}*/
/*
@Composable
fun Int.asUiImageParam(isHeightZero: Boolean = false): Painter {
    return when (this) {
        1 -> painterResource(R.drawable.not_available)
        2 -> if (isHeightZero) painterResource(R.drawable.thermometer) else painterResource(R.drawable.thermometer_exterior)
        3 -> painterResource(R.drawable.humidity)
        4 -> painterResource(R.drawable.pressure)
        5 -> painterResource(R.drawable.sea_level)
        else -> painterResource(R.drawable.not_available)
    }
}
*/

@Composable
fun MeasurementUnit?.asUiImageParam(isHeightZero: Boolean = false): Painter {
    return when (this?.abbreviation) {
        "°C" -> if (isHeightZero) painterResource(R.drawable.thermometer) else painterResource(R.drawable.thermometer_exterior)
        "%" -> painterResource(R.drawable.humidity)
        "мм.рт.ст." -> painterResource(R.drawable.pressure)
        "м." -> painterResource(R.drawable.sea_level)
        "в." -> painterResource(R.drawable.electric_bolt_24dp)
        else -> painterResource(R.drawable.not_available)
    }
}

@Composable
fun Int.asUiTextParam(): String {
    return when (this) {
        1 -> stringResource(R.string.unit_unknown)
        2 -> stringResource(R.string.unit_temperature)
        3 -> stringResource(R.string.unit_humidity)
        4 -> stringResource(R.string.unit_pressure)
        5 -> stringResource(R.string.unit_length)
        else -> stringResource(R.string.unit_unknown)
    }
}

@Composable
fun MeasurementUnit.asUiTextParam(): String {
    return when (this.name) {
        "°C" -> stringResource(R.string.unit_temperature)
        "%" -> stringResource(R.string.unit_humidity)
        "мм.рт.ст." -> stringResource(R.string.unit_pressure)
        "м." -> stringResource(R.string.unit_length)
        else -> stringResource(R.string.unit_unknown)
    }
}
