package com.edurda77.directories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton

@Composable
fun ItemDevicesGroupDirectory(
    modifier: Modifier = Modifier,
    title: String,
    titleDelete: String,
    isEnabledUpdate: Boolean,
    onDeleteClick: () -> Unit,
    onUpdateClick: (String) -> Unit,
) {
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isShowUpdateDialog = remember { mutableStateOf(false) }
    if (isShowDeleteDialog.value) {
        UiAlertDialog(
            title = titleDelete,
            onClickCancel = {
                isShowDeleteDialog.value = false
            },
            onClickConfirm = {
                isShowDeleteDialog.value = false
                onDeleteClick()
            }
        )
    }
    if (isShowUpdateDialog.value) {
        UiDialog(
            onCloseDialog = { isShowUpdateDialog.value = false },
            content = {
                UpdateDevicesGroupDialog(
                    currentName = title,
                    onCloseClick = { isShowUpdateDialog.value = false },
                    onUpdateClick = { name ->
                        onUpdateClick(name)
                    }
                )
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 2.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier,
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.labelSmall,
            )
            Spacer(modifier = modifier.weight(1f))
            if (isEnabledUpdate) {
                UiIconButton(
                    icon = Icons.Default.Edit,
                    onClick = { isShowUpdateDialog.value = true }
                )
                UiIconButton(
                    icon = Icons.Default.Delete,
                    onClick = { isShowDeleteDialog.value = true }
                )
            }
        }
        Spacer(modifier = modifier.height(5.dp))
        HorizontalDivider(
            modifier = modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}