package com.edurda77.directories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
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
    val localDensity = LocalDensity.current
    val offsetXDropDownMenu = remember { mutableStateOf(0.dp) }
    val expandedDropDownloads = remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier,
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = Typography.bodyLarge,
            )
            Spacer(modifier = modifier.weight(1f))
            if (isEnabledUpdate) {
                UiIconButton(
                    modifier = modifier
                        .onGloballyPositioned { coordinates ->
                            offsetXDropDownMenu.value =
                                with(localDensity) { coordinates.positionInRoot().x.toDp() }
                        },
                    icon = ImageVector.vectorResource(R.drawable.three_dots),
                    color = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        expandedDropDownloads.value = true
                    }
                )
                DropdownMenu(
                    expanded = expandedDropDownloads.value,
                    containerColor = MaterialTheme.colorScheme.background,
                    offset = DpOffset(x = offsetXDropDownMenu.value * 0.6f, y = 0.dp),
                    onDismissRequest = {
                        expandedDropDownloads.value = false
                    }
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.pencil),
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            isShowUpdateDialog.value = true
                        },
                        text = {
                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.update_value),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.labelSmall,
                            )
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.trashcan),
                                contentDescription = ""
                            )
                        },
                        onClick = {
                            isShowDeleteDialog.value = true
                        },
                        text = {
                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.delete_value),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.labelSmall,
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ItemDevicesGroupDirectoryView1() {
    ImpulsMeteoTheme {
        ItemDevicesGroupDirectory(
            title = "all",
            titleDelete = "delete",
            isEnabledUpdate = true,
            onDeleteClick = {},
            onUpdateClick = {},
        )
    }
}

@Preview
@Composable
private fun ItemUserView2() {
    ImpulsMeteoTheme {
        ItemDevicesGroupDirectory(
            title = "all",
            titleDelete = "delete",
            isEnabledUpdate = true,
            onDeleteClick = {},
            onUpdateClick = {},
        )
    }
}