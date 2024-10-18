package com.edurda77.devices_list

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
import com.edurda77.domain.model.GroupDevices
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@Composable
fun AddDeviceDialog(
    groups: List<GroupDevices>,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onAddClick: (String, String, String, List<GroupDevices>) -> Unit,
    onUpdateGroups: (GroupDevices) -> Unit,
    selectedGroups: List<GroupDevices>,
) {

    val name = remember { mutableStateOf("") }
    val key = remember { mutableStateOf("") }
    val frequency = remember { mutableStateOf("") }
    val expandedGroupMenu = remember { mutableStateOf(false) }
    val selectedGroupsText = remember { mutableStateOf("") }
    LaunchedEffect(selectedGroups.size) {
        selectedGroupsText.value = selectedGroups.joinToString { it.name }
    }

    Column {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.add),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = name.value,
            label = stringResource(id = R.string.name),
            onClickContent = {
                name.value = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = key.value,
            label = stringResource(id = R.string.key),
            onClickContent = {
                key.value = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = frequency.value,
            label = stringResource(id = R.string.update_frequency),
            onClickContent = {
                frequency.value = it
            },
            isOnlyDigit = true
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = selectedGroupsText.value,
            label = stringResource(id = R.string.group),
            onClickContent = {},
            trailingIcon = if (expandedGroupMenu.value) ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24)
            else ImageVector.vectorResource(
                id = R.drawable.baseline_arrow_drop_up_24
            ),
            readOnly = true,
            onClickTrailingIcon = {
                expandedGroupMenu.value = true
            },
            maxLines = 4
        )
        DropdownMenu(
            expanded = expandedGroupMenu.value,
            onDismissRequest = {
                expandedGroupMenu.value = false
            }) {
            groups.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = modifier
                                .background(if (selectedGroups.contains(it)) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .padding(4.dp),
                            text = it.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.labelSmall,
                        )
                    },
                    onClick = {
                        onUpdateGroups(it)
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
                enabled = name.value.isNotBlank() && key.value.isNotBlank() && frequency.value.isNotBlank() && selectedGroups.isNotEmpty(),
                onClick = {
                    onCloseClick()
                    onAddClick(
                        name.value,
                        key.value,
                        frequency.value,
                        selectedGroups
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
