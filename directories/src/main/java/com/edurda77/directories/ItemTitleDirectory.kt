package com.edurda77.directories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography

@Composable
fun ItemTitleDirectory(
    modifier: Modifier = Modifier,
    title: String,
    color: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .border(border = BorderStroke(width = 1.dp, color = color), shape = MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 10.dp)
            .clickable(
                onClick = onClick
            )
        ,
        text = title,
        color = color,
        style = Typography.labelSmall,
        textAlign = TextAlign.Center
    )
}

@Preview()
@Composable
fun ItemTitleDirectoryView1() {
    ImpulsMeteoTheme {
        ItemTitleDirectory(
            title = "All",
            color = MaterialTheme.colorScheme.background,
            backgroundColor = MaterialTheme.colorScheme.primary,
            onClick = {}
        )
    }
}