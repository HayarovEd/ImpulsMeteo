package com.edurda77.resources.uikit

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.edurda77.resources.theme.Typography

@Composable
fun UiRowDeviceValueWithClick(
    modifier: Modifier = Modifier,
    image: Painter,
    value: Double,
    unit: String,
    name: String,
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
    Row(
        modifier = modifier.clickable(onClick = onOpenClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = modifier.size(24.dp),
            painter = image,
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

@Preview(showBackground = true, backgroundColor = 0xF0F5E388)
@Composable
private fun Sample() {
    UiRowDeviceValue(
        image = painterResource(R.drawable.no_picture),
        value = 0.3,
        unit = stringResource(R.string.unit_temperature),
        name = "t° Улица 2"
    )
}