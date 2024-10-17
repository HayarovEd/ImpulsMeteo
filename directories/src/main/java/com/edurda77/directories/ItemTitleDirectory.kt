package com.edurda77.directories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edurda77.resources.theme.Typography

@Composable
fun ItemTitleDirectory(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
    colorDivider: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = color,
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(
            modifier = Modifier,
            thickness = 2.dp,
            color = colorDivider
        )
    }
}