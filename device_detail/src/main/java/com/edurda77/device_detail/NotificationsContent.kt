package com.edurda77.device_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Notifications
import com.edurda77.domain.model.Param
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.asUiIconParam

@Composable
fun NotificationsContent(
    modifier: Modifier = Modifier,
    onClickChangeVisibleBottomSheet: () -> Unit,
    name: String?,
    notifications: Notifications?,
    params: List<Param>,
    onAddNotificationToListClick: (Int, String, Int) -> Unit,
    onDeleteNotificationFromListClick: (Int) -> Unit,
    onUpdateNotificationInListClick: (Int, Int, Int, String, Int) -> Unit,
    onChangeStatusClick: () -> Unit,
    onUpdateNotificationClick: () -> Unit,
) {
    val expandedAddNotificationDialog = remember { mutableStateOf(false) }
    val expandedUpdateNotificationDialog = remember { mutableStateOf(false) }

    if (expandedAddNotificationDialog.value) {
        UiDialog(
            onCloseDialog = {
                expandedAddNotificationDialog.value = false
            },
            content = {
                AddNotificationDialog(
                    onCloseClick = {
                        expandedAddNotificationDialog.value = false
                    },
                    onConfirmClick = { idParam, condition, value ->
                        expandedAddNotificationDialog.value = false
                        onAddNotificationToListClick(
                            idParam,
                            condition,
                            value
                        )
                    },
                    params = params,
                )
            }
        )
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier,
                text = stringResource(R.string.edit_notification_of_device),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = Typography.bodyLarge,
            )
            IconButton(onClick = onClickChangeVisibleBottomSheet) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Text(
            modifier = modifier,
            text = name ?: "",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onChangeStatusClick
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = notifications?.deviceStatus == true,
                onCheckedChange = { onChangeStatusClick() }
            )
            Spacer(modifier = modifier.width(5.dp))
            Text(
                modifier = modifier,
                text = stringResource(R.string.notificate_to_change_status),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = Typography.labelSmall,
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier,
                text = stringResource(R.string.parameters),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = Typography.bodyLarge,
            )
            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.baseline_add_24),
                onClick = {
                    expandedAddNotificationDialog.value = true
                }
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            if (notifications?.notifications != null) {
                itemsIndexed(notifications.notifications) { index, notification ->
                    val intIcon = params.firstOrNull { it.id == notification.idParam }?.idUnit ?: 1
                    val description =
                        params.firstOrNull { it.id == notification.idParam }?.name ?: ""
                    val label = params.firstOrNull { it.id == notification.idParam }?.label ?: ""
                    if (expandedUpdateNotificationDialog.value) {
                        UiDialog(
                            onCloseDialog = {
                                expandedUpdateNotificationDialog.value = false
                            },
                            content = {
                                UpdateNotificationDialog(
                                    onCloseClick = {
                                        expandedUpdateNotificationDialog.value = false
                                    },
                                    onConfirmClick = { id, idParam, condition, value ->
                                        expandedUpdateNotificationDialog.value = false
                                        onUpdateNotificationInListClick(
                                            index,
                                            id,
                                            idParam,
                                            condition,
                                            value
                                        )
                                    },
                                    params = params,
                                    notificationDevice = notification
                                )
                            }
                        )
                    }
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedUpdateNotificationDialog.value = true
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = intIcon.asUiIconParam(),
                                contentDescription = ""
                            )
                            Spacer(modifier = modifier.width(10.dp))
                            Column {
                                Text(
                                    modifier = Modifier,
                                    text = description,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = Typography.labelSmall,
                                )
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    modifier = Modifier,
                                    text = label,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = Typography.labelSmall,
                                )
                            }
                        }
                        Text(
                            modifier = Modifier,
                            text = notification.condition,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            style = Typography.labelSmall,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier,
                                text = notification.value.toString(),
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = Typography.labelSmall,
                            )
                            UiIconButton(
                                icon = ImageVector.vectorResource(R.drawable.baseline_delete_24),
                                onClick = {
                                    onDeleteNotificationFromListClick(index)
                                }
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        TextButton(
            onClick = onUpdateNotificationClick
        ) {
            Text(
                modifier = modifier,
                text = stringResource(id = R.string.save),
                color = MaterialTheme.colorScheme.primary,
                style = Typography.bodyLarge,
            )
        }
    }
}