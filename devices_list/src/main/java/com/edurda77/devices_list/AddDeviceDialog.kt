package com.edurda77.devices_list

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.GroupDevices
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@OptIn(ExperimentalLayoutApi::class)
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
    val selectedGroupsText = remember { mutableStateOf("") }
    LaunchedEffect(selectedGroups.size) {
        selectedGroupsText.value = selectedGroups.joinToString { it.name }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.add_device),
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
            groups.forEach { group ->
                FilterChip(
                    label = {
                        Text(
                            modifier = modifier,
                            text = group.name,
                            style = Typography.labelSmall,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.outline
                    ),

                    selected = selectedGroups.contains(group),
                    onClick = {
                        onUpdateGroups(group)
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
                    onAddClick(
                        name.value,
                        key.value,
                        frequency.value,
                        selectedGroups
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
    showBackground = true
)
@Composable
private fun AddDeviceDialogView() {
    val groups = remember {
        (1..5).map {
            GroupDevices(
                id = it,
                name = "Group $it"
            )
        }
    }
    ImpulsMeteoTheme {
        AddDeviceDialog(
            onAddClick = { _, _, _, _ -> },
            onCloseClick = {},
            groups = groups,
            onUpdateGroups = {},
            selectedGroups = groups.filter { it.id % 2 == 0 }
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AddDeviceDialogView2() {
    val groups = remember {
        (1..5).map {
            GroupDevices(
                id = it,
                name = "Group $it"
            )
        }
    }
    ImpulsMeteoTheme {
        AddDeviceDialog(
            onAddClick = { _, _, _, _ -> },
            onCloseClick = {},
            groups = groups,
            onUpdateGroups = {},
            selectedGroups = groups.filter { it.id % 2 == 0 }
        )
    }
}