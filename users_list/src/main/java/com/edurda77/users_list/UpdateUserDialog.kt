package com.edurda77.users_list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.newModels.UserUi
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@OptIn(ExperimentalLayoutApi::class)
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
    onUpdateClick: (String, String, String, String, List<DeviceUser>, List<PermissionUser>) -> Unit,
) {
    val name = remember { mutableStateOf(user.name) }
    val email = remember { mutableStateOf(user.email) }
    val password = remember { mutableStateOf("") }
    var hidePassword by remember { mutableStateOf(true) }
    val selectedPermissionsText = remember { mutableStateOf("") }
    LaunchedEffect(selectedPermissions.size) {
        selectedPermissionsText.value = selectedPermissions.joinToString { it.displayName }
    }
    val selectedDevicessText = remember { mutableStateOf("") }
    LaunchedEffect(selectedDevices.size) {
        selectedDevicessText.value = selectedDevices.joinToString { it.name }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.update_user),
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
            visualTransformation = if (hidePassword) VisualTransformation.None else PasswordVisualTransformation(),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    hidePassword = !hidePassword
                }
            ),
            trailingIcon = if (hidePassword) ImageVector.vectorResource(R.drawable.baseline_visibility_off_24) else ImageVector.vectorResource(
                R.drawable.baseline_visibility_24
            ),
        )
        Spacer(modifier = modifier.height(5.dp))
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.access_rights),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        permissions.forEach { permission ->
            AccessRow(
                title = permission.displayName,
                isAccess = selectedPermissions.contains(permission),
                onClickAccess = {
                    onUpdatePermissions(permission)
                }
            )
        }
        Spacer(modifier = modifier.height(5.dp))
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.devices),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            devices.sortedBy { it.name }.forEach { device ->
                FilterChip(
                    label = {
                        Text(
                            modifier = modifier,
                            text = device.name,
                            style = Typography.labelSmall,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.outline
                    ),

                    selected = selectedDevices.contains(device),
                    onClick = {
                        onUpdateDevices(device)
                    }
                )
            }
        }
        Spacer(modifier = modifier.height(5.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                onClick = onCloseClick
            ) {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(id = R.string.cancel),
                    style = Typography.bodySmall,
                )
            }
            Spacer(modifier = modifier.width(10.dp))
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
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
                    color = MaterialTheme.colorScheme.background,
                    text = stringResource(id = R.string.ok),
                    style = Typography.bodySmall,
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UpdateUserDialogView1() {
    ImpulsMeteoTheme {
        UpdateUserDialog(
            user = UserUi(
                id = "0",
                email = "eeeee",
                name = "Edward",
                devices = listOf(
                    DeviceUser(
                        id = "1",
                        name = "Device 1"
                    ),
                    DeviceUser(
                        id = "2",
                        name = "Device 2"
                    )
                ),
                isEnabled = true,
                password = "",
                permissions = listOf(
                    PermissionUser(
                        id = "1",
                        displayName = "prm1"
                    ),
                    PermissionUser(
                        id = "1",
                        displayName = "prm1"
                    )
                )
            ),
            devices = listOf(
                DeviceUser(
                    id = "1",
                    name = "Device 1"
                ),
                DeviceUser(
                    id = "2",
                    name = "Device 2"
                )
            ),
            onUpdateClick = { _, _, _, _, _, _ -> },
            onUpdatePermissions = {},
            onUpdateDevices = {},
            permissions = listOf(
                PermissionUser(
                    id = "1",
                    displayName = "prm1"
                ),
                PermissionUser(
                    id = "1",
                    displayName = "prm1"
                )
            ),
            selectedPermissions = listOf(
                PermissionUser(
                    id = "1",
                    displayName = "prm1"
                ),
            ),
            selectedDevices = listOf(
                DeviceUser(
                    id = "2",
                    name = "Device 2"
                )
            ),
            onCloseClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun UpdateUserDialog2() {
    ImpulsMeteoTheme {
        UpdateUserDialog(
            user = UserUi(
                id = "0",
                email = "eeeee",
                name = "Edward",
                devices = listOf(
                    DeviceUser(
                        id = "1",
                        name = "Device 1"
                    ),
                    DeviceUser(
                        id = "2",
                        name = "Device 2"
                    )
                ),
                password = "dfddf",
                isEnabled = true,
                permissions = listOf(
                    PermissionUser(
                        id = "1",
                        displayName = "prm1"
                    ),
                    PermissionUser(
                        id = "1",
                        displayName = "prm1"
                    )
                )
            ),
            devices = listOf(
                DeviceUser(
                    id = "1",
                    name = "Device 1"
                ),
                DeviceUser(
                    id = "2",
                    name = "Device 2"
                )
            ),
            onUpdateClick = { _, _, _, _, _, _ -> },
            onUpdatePermissions = {},
            onUpdateDevices = {},
            permissions = listOf(
                PermissionUser(
                    id = "1",
                    displayName = "prm1"
                ),
                PermissionUser(
                    id = "1",
                    displayName = "prm1"
                )
            ),
            selectedPermissions = listOf(
                PermissionUser(
                    id = "1",
                    displayName = "prm1"
                ),
            ),
            selectedDevices = listOf(
                DeviceUser(
                    id = "2",
                    name = "Device 2"
                )
            ),
            onCloseClick = {}
        )
    }
}