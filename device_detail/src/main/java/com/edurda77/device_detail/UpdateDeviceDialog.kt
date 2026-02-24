package com.edurda77.device_detail

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.GroupDevice
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UpdateDeviceDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    label: String,
    key: String,
    frequency: Int,
    groups: List<GroupDevice>,
    onUpdateClick: (String, String, String) -> Unit,
    onUpdateGroups: (GroupDevice) -> Unit,
    selectedGroups: List<GroupDevice>,
) {
    var currentName by remember { mutableStateOf(label) }
    var currentKey by remember { mutableStateOf(key) }
    var currentFrequency by remember { mutableStateOf(frequency.toString()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.update_device),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = currentName,
            label = stringResource(id = R.string.name),
            onClickContent = {
                currentName = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = currentKey,
            label = stringResource(id = R.string.key),
            onClickContent = {
                currentKey = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = currentFrequency,
            label = stringResource(id = R.string.update_frequency),
            onClickContent = {
                currentFrequency = it
            },
            isOnlyDigit = true
        )
        Spacer(modifier = modifier.height(5.dp))
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.group),
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
                    onUpdateClick(
                        currentName,
                        currentKey,
                        currentFrequency,
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
private fun UpdateDeviceDialogView() {
    ImpulsMeteoTheme {
        UpdateDeviceDialog(
            onCloseClick = {},
            label = "label",
            key = "key",
            frequency = 60000,
            groups = listOf(
                GroupDevice(
                    id = "1",
                    name = "Perm"
                ),
                GroupDevice(
                    id = "2",
                    name = "All"
                )
            ),
            onUpdateClick = { _, _, _ -> },
            onUpdateGroups = {},
            selectedGroups = listOf(
                GroupDevice(
                    id = "1",
                    name = "Perm"
                )
            ),
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UpdateDeviceDialogView2() {
    ImpulsMeteoTheme {
        UpdateDeviceDialog(
            onCloseClick = {},
            label = "label",
            key = "key",
            frequency = 60000,
            groups = listOf(
                GroupDevice(
                    id = "1",
                    name = "Perm"
                ),
                GroupDevice(
                    id = "2",
                    name = "All"
                )
            ),
            onUpdateClick = { _, _, _ -> },
            onUpdateGroups = {},
            selectedGroups = listOf(
                GroupDevice(
                    id = "1",
                    name = "Perm"
                )
            ),
        )
    }
}
