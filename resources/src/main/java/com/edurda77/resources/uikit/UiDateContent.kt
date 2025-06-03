package com.edurda77.resources.uikit

import android.content.res.Configuration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography

@Composable
fun UiDateContent(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    icon: ImageVector? = ImageVector.vectorResource(R.drawable.baseline_calendar_month_24),
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier,
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.labelSmall,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = content,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.labelSmall,
            )
            if (icon != null) {
                UiIconButton(
                    icon = icon,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun UiDateContent1() {
    ImpulsMeteoTheme {
        UiDateContent(
            title = stringResource(R.string.date_from),
            content = "08-03-2025",
            onClick = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiDateContent2() {
    ImpulsMeteoTheme {
        UiDateContent(
            title = stringResource(R.string.date_from),
            content = "08-03-2025",
            onClick = {}
        )
    }
}