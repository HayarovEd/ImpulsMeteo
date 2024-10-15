package com.edurda77.resources.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography

@Composable
fun UiAlertDialog(
    title: String,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                modifier = modifier
                    .clip(shape = RoundedCornerShape(5.dp))
                    .padding(5.dp),
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = Typography.bodyLarge,
            )
        },
        onDismissRequest = onClickCancel,
        dismissButton = {
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                onClick = onClickCancel
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.bodySmall,
                )
            }
        },
        confirmButton = {
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                onClick = onClickConfirm
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = Typography.bodySmall,
                )
            }
        },
        shape = RoundedCornerShape(15.dp)
    )
}