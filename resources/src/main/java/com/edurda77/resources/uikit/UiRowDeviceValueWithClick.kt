package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme

@Composable
fun UiRowDeviceValueWithClick(
    modifier: Modifier = Modifier,
    image: Painter,
    value: Double,
    unit: String,
    name: String,
    hexColor: String,
    content: @Composable () -> Unit,
    onOpenClick: () -> Unit,
    onCloseClick: () -> Unit,
    expandedDialog: Boolean
) {
    if (expandedDialog) {
        UiDialog(
            content = content,
            onCloseDialog = onCloseClick
        )
    }
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = MaterialTheme.shapes.small,
        onClick = onOpenClick,
    ) {
        UiRowContent(
            image = image,
            value = value,
            unit = unit,
            name = name,
            hexColor = hexColor
        )
    }
}

@Preview
@Composable
private fun Sample() {
    ImpulsMeteoTheme {
        UiRowDeviceValueWithClick(
            image = painterResource(R.drawable.full_screen),
            value = 0.3,
            unit = stringResource(R.string.unit_temperature),
            name = "t° Улица 2",
            content = {},
            onCloseClick = {},
            onOpenClick = {},
            expandedDialog = false,
            hexColor = "#000000",
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Sample2() {
    ImpulsMeteoTheme {
        UiRowDeviceValueWithClick(
            image = painterResource(R.drawable.full_screen),
            value = 0.3,
            unit = stringResource(R.string.unit_temperature),
            name = "t° Улица 2",
            content = {},
            onCloseClick = {},
            onOpenClick = {},
            hexColor = "#FFFFFF",
            expandedDialog = false
        )
    }
}