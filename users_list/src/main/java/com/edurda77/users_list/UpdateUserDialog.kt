package com.edurda77.users_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField
import com.edurda77.users_list.model.UserUi

@Composable
fun UpdateUserDialog(
    modifier: Modifier = Modifier,
    user: UserUi,
    devices: List<DeviceUser>,
    permissions: List<PermissionUser>,
    selectedPermissions: List<PermissionUser>,
    selectedDevices: List<DeviceUser>,
    onCloseClick: () -> Unit,
    onUpdatePermissions: (PermissionUser) -> Unit,
    onUpdateDevices: (DeviceUser) -> Unit,
    onUpdateClick: (Int, String, String, String, List<DeviceUser>, List<PermissionUser>) -> Unit,
) {
    val name = remember { mutableStateOf(user.name) }
    val email = remember { mutableStateOf(user.email) }
    val password = remember { mutableStateOf("") }
    val expandedPermissionsMenu = remember { mutableStateOf(false) }
    val selectedPermissionsText = remember { mutableStateOf("") }
    LaunchedEffect(selectedPermissions.size) {
        selectedPermissionsText.value = selectedPermissions.joinToString { it.displayName }
    }
    val expandedDevicesMenu = remember { mutableStateOf(false) }
    val selectedDevicessText = remember { mutableStateOf("") }
    LaunchedEffect(selectedDevices.size) {
        selectedDevicessText.value = selectedDevices.joinToString { it.name }
    }

    Column {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.add_user),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = name.value,
            label = stringResource(id = R.string.title),
            onClickContent = {
                name.value = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = email.value,
            label = stringResource(id = R.string.email),
            onClickContent = {
                email.value = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = password.value,
            label = stringResource(id = R.string.password),
            onClickContent = {
                password.value = it
            },
            isOnlyDigit = true
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = selectedPermissionsText.value,
            label = stringResource(id = R.string.access_rights),
            onClickContent = {},
            trailingIcon = if (expandedPermissionsMenu.value) ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24)
            else ImageVector.vectorResource(
                id = R.drawable.baseline_arrow_drop_up_24
            ),
            readOnly = true,
            onClickTrailingIcon = {
                expandedPermissionsMenu.value = true
            },
            maxLines = 4
        )
        DropdownMenu(
            expanded = expandedPermissionsMenu.value,
            onDismissRequest = {
                expandedPermissionsMenu.value = false
            }) {
            permissions.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = modifier
                                .background(if (selectedPermissions.contains(it)) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .padding(4.dp),
                            text = it.displayName,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.labelSmall,
                        )
                    },
                    onClick = {
                        onUpdatePermissions(it)
                    },
                )
            }
        }
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = selectedDevicessText.value,
            label = stringResource(id = R.string.devices),
            onClickContent = {},
            trailingIcon = if (expandedPermissionsMenu.value) ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24)
            else ImageVector.vectorResource(
                id = R.drawable.baseline_arrow_drop_up_24
            ),
            readOnly = true,
            onClickTrailingIcon = {
                expandedDevicesMenu.value = true
            },
            maxLines = 4
        )
        DropdownMenu(
            expanded = expandedDevicesMenu.value,
            onDismissRequest = {
                expandedDevicesMenu.value = false
            }) {
            devices.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = modifier
                                .background(if (selectedDevices.contains(it)) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .padding(4.dp),
                            text = it.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.labelSmall,
                        )
                    },
                    onClick = {
                        onUpdateDevices(it)
                    },
                )
            }
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = onCloseClick
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = modifier.width(10.dp))
            Button(
                modifier = modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    onCloseClick()
                    onUpdateClick(
                        user.id,
                        name.value,
                        email.value,
                        password.value,
                        selectedDevices,
                        selectedPermissions
                    )
                }
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    }
}