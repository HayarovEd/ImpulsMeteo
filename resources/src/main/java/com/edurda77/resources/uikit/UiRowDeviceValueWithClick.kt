package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.utils.hexToBrush

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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = MaterialTheme.shapes.small,
        onClick = onOpenClick,
        /*colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )*/
    ) {
        Row(
            modifier = modifier
                .background(brush = hexToBrush(hexColor))
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = modifier.size(24.dp),
                painter = image,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = "",
            )
            Spacer(modifier = modifier.width(10.dp))
            Column {
                Text(
                    modifier = modifier
                        .basicMarquee(),
                    text = "$value $unit",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.width(5.dp))
                Text(
                    modifier = modifier
                        .basicMarquee(),
                    text = name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.labelSmall,
                )
            }
        }
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
            hexColor = "#50e3c2",
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
            hexColor = "#50e3c2",
            expandedDialog = false
        )
    }
}