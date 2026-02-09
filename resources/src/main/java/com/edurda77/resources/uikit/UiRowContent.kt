package com.edurda77.resources.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.utils.getContrastColor
import com.edurda77.resources.utils.hexToBrush

@Composable
internal fun UiRowContent(
    image: Painter,
    value: Double,
    unit: String,
    name: String,
    hexColor: String,
) {
    val textColor = getContrastColor(hexColor)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = hexToBrush(hexColor))
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = image,
            tint = textColor,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                modifier = Modifier
                    .basicMarquee(),
                text = "${"%.1f".format(value)} $unit",
                color = textColor,
                style = Typography.bodyLarge,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .basicMarquee(),
                text = name,
                color = textColor,
                style = Typography.labelSmall,
            )
        }
    }
}