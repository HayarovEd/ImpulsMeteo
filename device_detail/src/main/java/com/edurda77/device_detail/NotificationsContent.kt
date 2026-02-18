package com.edurda77.device_detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.model.newModels.NotificationDevice
import com.edurda77.domain.model.newModels.NotificationParam
import com.edurda77.domain.model.newModels.Param
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField
import com.edurda77.resources.uikit.asUiImageParam

@Composable
fun NotificationsContent(
    modifier: Modifier = Modifier,
    onClickChangeVisibleBottomSheet: () -> Unit,
    notifications: NotificationDevice?,
    params: List<Param>,
    onAddNotificationToListClick: (String, String, Int) -> Unit,
    onDeleteNotificationFromListClick: (Int) -> Unit,
    onUpdateNotificationInListClick: (Int, Int, Int, String, Int) -> Unit,
    onChangeStatusClick: () -> Unit,
    onUpdateNotificationClick: () -> Unit,
) {
    val expandedUpdateNotificationDialog = remember { mutableStateOf(false) }
    val expandedParameters = remember { mutableStateOf(false) }
    val parameter = remember { mutableStateOf("") }
    val parameterId = remember { mutableStateOf("") }
    val conditions = listOf("<=", ">=")
    val currentCondition = remember { mutableStateOf(conditions.first()) }
    val value = remember { mutableStateOf("") }

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
                color = MaterialTheme.colorScheme.onBackground,
                style = Typography.bodyLarge,
            )
            IconButton(onClick = onClickChangeVisibleBottomSheet) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onChangeStatusClick
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier,
                text = stringResource(R.string.notificate_to_change_status),
                color = MaterialTheme.colorScheme.onBackground,
                style = Typography.labelSmall,
            )
            Spacer(modifier = modifier.width(5.dp))
            Switch(
                checked = notifications?.value == true,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White
                ),
                onCheckedChange = { onChangeStatusClick() }
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                UiTextField(
                    content = parameter.value,
                    label = stringResource(R.string.parameter),
                    onClickContent = {},
                    readOnly = true,
                    trailingIcon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
                    onClickTrailingIcon = {
                        expandedParameters.value = true
                    }
                )
                DropdownMenu(
                    modifier = modifier,
                    //offset = DpOffset(x = offsetXDropDownMenu.value, y = 0.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    expanded = expandedParameters.value,
                    onDismissRequest = { expandedParameters.value = false }
                ) {
                    params.forEach { param ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = param.label,
                                    style = Typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }, onClick = {
                                parameter.value = param.label
                                parameterId.value = param.id
                                expandedParameters.value = false
                            })
                    }
                }
            }
            /*Text(
                modifier = modifier,
                text = stringResource(R.string.parameter),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.bodyLarge,
            )
            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.baseline_add_24),
                onClick = {
                    expandedAddNotificationDialog.value = true
                }
            )*/
        }
        Spacer(modifier = modifier.height(10.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentCondition.value==conditions.first()) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (currentCondition.value==conditions.first()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    currentCondition.value = conditions.first()
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (currentCondition.value!=conditions.first()) MaterialTheme.colorScheme.primary else Color.Transparent)
            ) {
                Text(
                    text = conditions.first(),
                    style = Typography.bodySmall,
                )
            }
            Button(
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentCondition.value==conditions.last()) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (currentCondition.value==conditions.last()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    currentCondition.value = conditions.last()
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (currentCondition.value!=conditions.last()) MaterialTheme.colorScheme.primary else Color.Transparent)
            ) {
                Text(
                    text = conditions.last(),
                    style = Typography.bodySmall,
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        UiTextField(
            content = value.value,
            label = stringResource(R.string.value),
            isOnlyDigit = true,
            onClickContent = {
                value.value = it
            }
        )
        Spacer(modifier = modifier.height(10.dp))
        Button(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                onAddNotificationToListClick(
                    parameterId.value,
                    currentCondition.value,
                    value.value.toIntOrNull() ?: 0
                )
            }
        ) {
            Text(
                color = MaterialTheme.colorScheme.background,
                text = stringResource(id = R.string.add),
                style = Typography.bodySmall,
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        notifications?.let { nf->
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth(),
            ) {
                itemsIndexed(nf.notificationParam) { index, notificationParam ->
                    val currentNf = params.firstOrNull { it.id == notificationParam.paramId }
                    val intIcon = currentNf?.measurementUnit
                    val description = currentNf?.name
                    val valueNotification = currentNf?.value
                    if (expandedUpdateNotificationDialog.value) {
                        UiDialog(
                            onCloseDialog = {
                                expandedUpdateNotificationDialog.value = false
                            },
                            content = {
                               /* UpdateNotificationDialog(
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
                                    notificationDeviceOld = notificationParam
                                )*/
                            }
                        )
                    }
                    Row(
                        modifier = modifier
                            .clip(shape = MaterialTheme.shapes.medium)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .clickable {
                                expandedUpdateNotificationDialog.value = true
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            modifier = modifier.size(24.dp),
                            painter = intIcon.asUiImageParam(),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier,
                            text = "$description ${notificationParam.condition} $valueNotification",
                            color = Color.White,
                            style = Typography.labelSmall,
                        )
                        Spacer(modifier = modifier.weight(1f))
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
        Spacer(modifier = modifier.height(10.dp))
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
                onClick = onClickChangeVisibleBottomSheet
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
                onClick = onUpdateNotificationClick
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
private fun NotificationsContentView() {
    val params = remember {
        (0..5).map {
            Param(
                id = "$it",
                name = "Param $it",
                label = "label",
                value = it + 5.0,
                color = "#50e3c2",
                classIcon = "wi wi-thermometer",
                isHidden = false,
                measurementUnit = MeasurementUnit(
                    abbreviation = "°C",
                    id = "6",
                    name = "temp"
                )
            )
        }
    }
    ImpulsMeteoTheme {
        NotificationsContent(
            onUpdateNotificationClick = {},
            onUpdateNotificationInListClick = { _, _, _, _, _ -> },
            onClickChangeVisibleBottomSheet = {},
            onChangeStatusClick = {},
            onAddNotificationToListClick = { _, _, _ -> },
            onDeleteNotificationFromListClick = {},
            notifications = NotificationDevice(
               deviceId = "0",
                id = "1",
                notificationParam = (1..3).map {
                    NotificationParam(
                        condition = "<",
                        id = "$it",
                        isSend = true,
                        paramId = "1",
                        userId = "1",
                        value = 3
                    )
                },
                userId = "1",
                value = true
            ),
            params = params
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationsContentView2() {
    val params = remember {
        (0..5).map {
            Param(
                id = "$it",
                name = "Param $it",
                label = "label",
                value = it + 5.0,
                color = "#50e3c2",
                classIcon = "wi wi-thermometer",
                isHidden = false,
                measurementUnit = MeasurementUnit(
                    abbreviation = "°C",
                    id = "6",
                    name = "temp"
                )
            )
        }
    }
    ImpulsMeteoTheme {
        NotificationsContent(
            onUpdateNotificationClick = {},
            onUpdateNotificationInListClick = { _, _, _, _, _ -> },
            onClickChangeVisibleBottomSheet = {},
            onChangeStatusClick = {},
            onAddNotificationToListClick = { _, _, _ -> },
            onDeleteNotificationFromListClick = {},
            notifications = NotificationDevice(
                deviceId = "0",
                id = "1",
                notificationParam = (1..3).map {
                    NotificationParam(
                        condition = "<",
                        id = "$it",
                        isSend = true,
                        paramId = "1",
                        userId = "1",
                        value = 3
                    )
                },
                userId = "1",
                value = true
            ),
            params = params
        )
    }
}

