package com.edurda77.resources.uikit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography

@Composable
fun UiDateContent(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onClick: () -> Unit
) {
    Column {
        Text(
            modifier = modifier,
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.labelSmall,
        )
        Spacer(modifier = modifier.height(3.dp))
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier,
                text = content,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.labelSmall,
            )
            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.baseline_calendar_month_24),
                onClick = onClick
            )
        }
    }
}