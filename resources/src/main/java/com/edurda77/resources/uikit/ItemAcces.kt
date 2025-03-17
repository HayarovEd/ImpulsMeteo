package com.edurda77.resources.uikit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
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
fun ItemAccess(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.checkmark),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.outlineVariant
        )
        Spacer(modifier = modifier.width(5.dp))
        Text(
            modifier = modifier,
            text = title,
            color = MaterialTheme.colorScheme.outline,
            style = Typography.labelSmall,
        )
    }
}