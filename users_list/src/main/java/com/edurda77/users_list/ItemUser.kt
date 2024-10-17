package com.edurda77.users_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.User
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton

@Composable
fun ItemUser(
    modifier: Modifier = Modifier,
    isEnabledDelete: Boolean,
    isEnabledUpdate: Boolean,
    user: User,
    onDeleteClick: (Int) -> Unit,
    onClearSelected: () -> Unit,
    onUpdateSelected: (Int) -> Unit,
    devices: List<DeviceUser>,
    permissions: List<PermissionUser>,
    selectedDevices: List<DeviceUser>,
    selectedPermissions: List<PermissionUser>,
    onUpdatePermissions: (PermissionUser) -> Unit,
    onUpdateDevices: (DeviceUser) -> Unit,
    onUpdateClick: (Int, String, String, String, List<DeviceUser>, List<PermissionUser>) -> Unit,
) {
    val expandedDeleteDialog = remember { mutableStateOf(false) }
    val expandedUpdateDialog = remember { mutableStateOf(false) }
    if (expandedDeleteDialog.value) {
        UiAlertDialog(
            title = stringResource(R.string.sure_delete_user),
            onClickConfirm = {
                onDeleteClick(user.id)
                expandedDeleteDialog.value = false
            },
            onClickCancel = {
                expandedDeleteDialog.value = false
            }
        )
    }
    if (expandedUpdateDialog.value) {
        UiDialog(
            onCloseDialog = {
                expandedUpdateDialog.value = false
                onClearSelected()
            },
            content = {
                UpdateUserDialog(
                    devices = devices,
                    permissions = permissions,
                    selectedDevices = selectedDevices,
                    selectedPermissions = selectedPermissions,
                    onCloseClick = {
                        expandedUpdateDialog.value = false
                        onClearSelected()
                    },
                    onUpdateClick = { id, name, email, password, devices, permissions ->
                        onUpdateClick(
                            id,
                            name,
                            email,
                            password,
                            devices,
                            permissions
                        )
                    },
                    onUpdatePermissions = {
                        onUpdatePermissions(it)
                    },
                    onUpdateDevices = {
                        onUpdateDevices(it)
                    },
                    user = user
                )
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            // .clickable(onClick = onClick)
            .padding(10.dp),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    modifier = modifier,
                    text = user.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.titleLarge,
                )
                Spacer(modifier = modifier.height(5.dp))
                Text(
                    modifier = modifier,
                    text = user.email,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
            }
            Row {
                if (isEnabledUpdate) {
                    UiIconButton(
                        icon = Icons.Default.Edit,
                        onClick = {
                            expandedUpdateDialog.value = true
                            onUpdateSelected(user.id)
                        }
                    )
                }
                if (isEnabledDelete) {
                    UiIconButton(
                        icon = Icons.Default.Delete,
                        onClick = {
                            expandedDeleteDialog.value = true
                        }
                    )
                }
            }
        }
        Spacer(modifier = modifier.height(2.dp))
        HorizontalDivider(
            modifier = modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(2.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = stringResource(R.string.access),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.bodyLarge,
            )
            Spacer(modifier = modifier.width(10.dp))
            Column(modifier = modifier.weight(2f)) {
                user.permissions.forEach {
                    Text(
                        modifier = modifier,
                        text = it.displayName,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.labelSmall,
                    )
                    Spacer(modifier = modifier.height(2.dp))
                }
            }
        }
        HorizontalDivider(
            modifier = modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(2.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = stringResource(R.string.devices),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.bodyLarge,
            )
            Spacer(modifier = modifier.width(10.dp))
            Column(modifier = modifier.weight(2f)) {
                user.devices.forEach {
                    Text(
                        modifier = modifier,
                        text = it.name,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.labelSmall,
                    )
                    Spacer(modifier = modifier.height(2.dp))
                }
            }
        }
    }
}