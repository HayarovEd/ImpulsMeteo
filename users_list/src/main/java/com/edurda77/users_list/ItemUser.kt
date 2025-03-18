package com.edurda77.users_list

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.ItemAccess
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.users_list.model.UserUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemUser(
    modifier: Modifier = Modifier,
    isEnabledDelete: Boolean,
    isEnabledUpdate: Boolean,
    isExpanded: Boolean,
    user: UserUi,
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
    onClickExpanded: () -> Unit,
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
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onClickExpanded() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = modifier.size(36.dp),
                    painter = painterResource(R.drawable.person),
                    contentDescription = ""
                )
                Spacer(modifier = modifier.width(10.dp))
                Column {
                    Text(
                        modifier = modifier.basicMarquee(),
                        text = user.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = Typography.bodyLarge,
                    )
                    Spacer(modifier = modifier.height(5.dp))
                    Text(
                        modifier = modifier.basicMarquee(),
                        text = user.email,
                        color = MaterialTheme.colorScheme.outline,
                        style = Typography.labelSmall,
                    )
                }
                Spacer(modifier = modifier.weight(1f))
                if (isEnabledUpdate) {
                    UiIconButton(
                        icon = ImageVector.vectorResource(R.drawable.pencil),
                        color = MaterialTheme.colorScheme.background,
                        buttonColor = MaterialTheme.colorScheme.primary,
                        onClick = {
                            expandedUpdateDialog.value = true
                            onUpdateSelected(user.id)
                        }
                    )
                }
                if (isEnabledDelete) {
                    UiIconButton(
                        icon = ImageVector.vectorResource(R.drawable.trashcan),
                        color = MaterialTheme.colorScheme.background,
                        buttonColor = MaterialTheme.colorScheme.error,
                        onClick = {
                            expandedDeleteDialog.value = true
                        }
                    )
                }
                UiIconButton(
                    icon = if (isExpanded) ImageVector.vectorResource(R.drawable.arrow_top) else ImageVector.vectorResource(
                        R.drawable.arrow_bottom
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    onClick = onClickExpanded
                )
            }
            if (isExpanded) {
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    modifier = modifier,
                    text = stringResource(R.string.access),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(5.dp))
                user.permissions.forEach {
                    ItemAccess(
                        title = it.displayName
                    )
                    Spacer(modifier = modifier.height(2.dp))
                }
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    modifier = modifier,
                    text = stringResource(R.string.devices),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(5.dp))
                FlowRow(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    user.devices.forEach {
                        Text(
                            modifier = modifier
                                .clip(shape = MaterialTheme.shapes.extraSmall)
                                .background(color = MaterialTheme.colorScheme.inverseSurface)
                                .padding(2.dp),
                            text = it.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ItemUserView1() {
    ImpulsMeteoTheme {
        ItemUser(
            isEnabledDelete = true,
            isEnabledUpdate = true,
            isExpanded = true,
            user = UserUi(
                id = 0,
                email = "eeeee",
                name = "Edward",
                devices = listOf(
                    DeviceUser(
                        id = 1,
                        name = "Device 1"
                    ),
                    DeviceUser(
                        id = 2,
                        name = "Device 2"
                    )
                ),
                permissions = listOf(
                    PermissionUser(
                        id = 1,
                        displayName = "prm1"
                    ),
                    PermissionUser(
                        id = 1,
                        displayName = "prm1"
                    )
                )
            ),
            devices = listOf(
                DeviceUser(
                    id = 1,
                    name = "Device 1"
                ),
                DeviceUser(
                    id = 2,
                    name = "Device 2"
                )
            ),
            onClickExpanded = {},
            onDeleteClick = {},
            onClearSelected = {},
            onUpdateClick = { id, name, email, password, devices, permissions -> },
            onUpdatePermissions = {},
            onUpdateSelected = {},
            onUpdateDevices = {},
            permissions = listOf(
                PermissionUser(
                    id = 1,
                    displayName = "prm1"
                ),
                PermissionUser(
                    id = 1,
                    displayName = "prm1"
                )
            ),
            selectedPermissions = listOf(
                PermissionUser(
                    id = 1,
                    displayName = "prm1"
                ),
            ),
            selectedDevices = listOf(
                DeviceUser(
                    id = 2,
                    name = "Device 2"
                )
            )
        )
    }
}

@Preview
@Composable
private fun ItemUserView2() {
    ImpulsMeteoTheme {
        ItemUser(
            isEnabledDelete = true,
            isEnabledUpdate = true,
            isExpanded = true,
            user = UserUi(
                id = 0,
                email = "eeeee",
                name = "Edward",
                devices = listOf(
                    DeviceUser(
                        id = 1,
                        name = "Device 1"
                    ),
                    DeviceUser(
                        id = 2,
                        name = "Device 2"
                    )
                ),
                permissions = listOf(
                    PermissionUser(
                        id = 1,
                        displayName = "prm1"
                    ),
                    PermissionUser(
                        id = 1,
                        displayName = "prm1"
                    )
                )
            ),
            devices = listOf(
                DeviceUser(
                    id = 1,
                    name = "Device 1"
                ),
                DeviceUser(
                    id = 2,
                    name = "Device 2"
                )
            ),
            onClickExpanded = {},
            onDeleteClick = {},
            onClearSelected = {},
            onUpdateClick = { id, name, email, password, devices, permissions -> },
            onUpdatePermissions = {},
            onUpdateSelected = {},
            onUpdateDevices = {},
            permissions = listOf(
                PermissionUser(
                    id = 1,
                    displayName = "prm1"
                ),
                PermissionUser(
                    id = 1,
                    displayName = "prm1"
                )
            ),
            selectedPermissions = listOf(
                PermissionUser(
                    id = 1,
                    displayName = "prm1"
                ),
            ),
            selectedDevices = listOf(
                DeviceUser(
                    id = 2,
                    name = "Device 2"
                )
            )
        )
    }
}