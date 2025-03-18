package com.edurda77.resources.uikit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun UiDialog(
    modifier: Modifier = Modifier,
    onCloseDialog: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onCloseDialog) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            content()
        }
    }
}