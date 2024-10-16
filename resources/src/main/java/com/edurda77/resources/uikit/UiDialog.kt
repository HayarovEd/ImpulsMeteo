package com.edurda77.resources.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun UiDialog(
    modifier: Modifier = Modifier,
    onCloseDialog: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onCloseDialog) {
        Column(
            modifier = modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
                .padding(10.dp)
        ) {
            content()
        }
    }
}